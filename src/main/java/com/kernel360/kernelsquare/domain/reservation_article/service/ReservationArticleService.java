package com.kernel360.kernelsquare.domain.reservation_article.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kernel360.kernelsquare.domain.coffeechat.entity.ChatRoom;
import com.kernel360.kernelsquare.domain.coffeechat.repository.CoffeeChatRepository;
import com.kernel360.kernelsquare.domain.hashtag.dto.FindHashtagResponse;
import com.kernel360.kernelsquare.domain.hashtag.dto.UpdateHashtagRequest;
import com.kernel360.kernelsquare.domain.hashtag.entity.Hashtag;
import com.kernel360.kernelsquare.domain.hashtag.repository.HashtagRepository;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.member.repository.MemberRepository;
import com.kernel360.kernelsquare.domain.reservation.dto.FindReservationResponse;
import com.kernel360.kernelsquare.domain.reservation.dto.UpdateReservationRequest;
import com.kernel360.kernelsquare.domain.reservation.entity.Reservation;
import com.kernel360.kernelsquare.domain.reservation.repository.ReservationRepository;
import com.kernel360.kernelsquare.domain.reservation_article.dto.CreateReservationArticleRequest;
import com.kernel360.kernelsquare.domain.reservation_article.dto.CreateReservationArticleResponse;
import com.kernel360.kernelsquare.domain.reservation_article.dto.FindAllReservationArticleResponse;
import com.kernel360.kernelsquare.domain.reservation_article.dto.FindReservationArticleResponse;
import com.kernel360.kernelsquare.domain.reservation_article.dto.UpdateReservationArticleRequest;
import com.kernel360.kernelsquare.domain.reservation_article.entity.ReservationArticle;
import com.kernel360.kernelsquare.domain.reservation_article.repository.ReservationArticleRepository;
import com.kernel360.kernelsquare.global.common_response.error.code.MemberErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.code.ReservationArticleErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import com.kernel360.kernelsquare.global.dto.PageResponse;
import com.kernel360.kernelsquare.global.dto.Pagination;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationArticleService {
	private final MemberRepository memberRepository;
	private final ReservationArticleRepository reservationArticleRepository;
	private final ReservationRepository reservationRepository;
	private final CoffeeChatRepository coffeeChatRepository;
	private final HashtagRepository hashtagRepository;

	@Transactional
	public CreateReservationArticleResponse createReservationArticle(
		CreateReservationArticleRequest createReservationArticleRequest) {
		Member member = memberRepository.findById(createReservationArticleRequest.memberId())
			.orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

		ReservationArticle reservationArticle = CreateReservationArticleRequest.toEntity(
			createReservationArticleRequest, member);
		ReservationArticle saveReservationArticle = reservationArticleRepository.save(reservationArticle);

		// HashTag 저장
		for (String hashTags : createReservationArticleRequest.hashTags()) {
			Hashtag hashTag = Hashtag.builder()
				.content(hashTags)
				.reservationArticle(saveReservationArticle)
				.build();

			hashtagRepository.save(hashTag);
		}

		for (LocalDateTime dateTime : createReservationArticleRequest.dateTimes()) {
			// 새로운 Chatroom 생성
			ChatRoom chatroom = ChatRoom.builder()
				.roomKey(UUID.randomUUID().toString())
				.expirationTime(dateTime.plusMinutes(30))
				.build();

			coffeeChatRepository.save(chatroom);

			// Reservation 생성 및 설정
			Reservation reservation = Reservation.builder()
				.startTime(dateTime)
				.endTime(dateTime.plusMinutes(30))
				.reservationArticle(saveReservationArticle)
				.chatRoom(chatroom)
				.build();

			reservationRepository.save(reservation);
		}

		return CreateReservationArticleResponse.from(saveReservationArticle);
	}

	@Transactional(readOnly = true)
	public PageResponse<FindAllReservationArticleResponse> findAllReservationArticle(Pageable pageable) {

		Integer currentPage = pageable.getPageNumber() + 1;
		Page<ReservationArticle> pages = reservationArticleRepository.findAll(pageable);
		Integer totalPages = pages.getTotalPages();

		if (totalPages == 0)
			totalPages += 1;

		if (currentPage > totalPages) {
			throw new BusinessException(ReservationArticleErrorCode.PAGE_NOT_FOUND);
		}

		Pagination pagination = Pagination.toEntity(totalPages, pages.getSize(), currentPage.equals(totalPages));

		List<FindAllReservationArticleResponse> responsePages = pages.getContent().stream()
			.map(article -> {
				Long fullCheck = reservationRepository.countByReservationArticleIdAndMemberIdIsNull(article.getId());
				return FindAllReservationArticleResponse.of(
					article.getMember(),
					article,
					fullCheck
				);
			})
			.toList();

		return PageResponse.of(pagination, responsePages);
	}

	@Transactional(readOnly = true)
	public FindReservationArticleResponse findReservationArticle(Long postId) {
		ReservationArticle reservationArticle = reservationArticleRepository.findById(postId)
			.orElseThrow(() -> new BusinessException(ReservationArticleErrorCode.RESERVATION_ARTICLE_NOT_FOUND));
		Member member = reservationArticle.getMember();

		List<Hashtag> hashtags = hashtagRepository.findAllByReservationArticleId(postId);
		List<FindHashtagResponse> findHashtagResponses = hashtags.stream()
			.map(FindHashtagResponse::from)
			.toList();

		List<Reservation> reservations = reservationRepository.findAllByReservationArticleId(postId);
		List<FindReservationResponse> findReservationResponses = reservations.stream()
			.map(FindReservationResponse::from)
			.toList();

		return FindReservationArticleResponse.of(member, reservationArticle, findHashtagResponses,
			findReservationResponses);
	}

	@Transactional
	public void updateReservationArticle(Long postId, UpdateReservationArticleRequest updateReservationArticleRequest) {
		ReservationArticle reservationArticle = reservationArticleRepository.findById(postId)
			.orElseThrow(() -> new BusinessException(ReservationArticleErrorCode.RESERVATION_ARTICLE_NOT_FOUND));

		// 멘토인지, 본인이 쓴 글인지 확인
		if (!reservationArticle.getMember().getId().equals(updateReservationArticleRequest.memberId())) {
			throw new BusinessException(ReservationArticleErrorCode.MENTOR_MISMATCH);
		}

		// 제목이나 내용은 ReservationArticle 의 update 로 변경
		reservationArticle.update(updateReservationArticleRequest.title(), updateReservationArticleRequest.content());

		// changehashtags 가 존재한다면 아래 로직
		if (updateReservationArticleRequest.changeHashtags() != null) {
			List<UpdateHashtagRequest> changeHashtags = updateReservationArticleRequest.changeHashtags();
			List<String> addHashtags = new ArrayList<>();
			Map<Long, String> removeHashtags = new HashMap<>();

			int addHashtagCount = 0;
			int removeHashtagCount = 0;
			for (UpdateHashtagRequest hashtagRequest : changeHashtags) {
				if (hashtagRequest.changed().equals("add")) {
					addHashtagCount++;
					addHashtags.add(hashtagRequest.content());
				} else if (hashtagRequest.changed().equals("remove")) {
					removeHashtagCount++;
					removeHashtags.put(hashtagRequest.hashtagId(), hashtagRequest.content());
				} else {
					throw new BusinessException(ReservationArticleErrorCode.STATUS_NOT_FOUND);
				}
			}

			// 수정된 해시태그 5개 넘는지 체크로직
			long currentHashtagCount = hashtagRepository.countAllByReservationArticleId(postId);
			long updatedHashtagCount = currentHashtagCount + addHashtagCount - removeHashtagCount;
			if (updatedHashtagCount >= 5) {
				throw new BusinessException(ReservationArticleErrorCode.TOO_MANY_HASHTAG);
			}

			// 체크로직을 통과했다면 add 는 더하고 remove 는 제거 (repository 로 반영)
			removeHashtags.forEach((id, content) -> hashtagRepository.deleteById(id));
			for (String hashTags : addHashtags) {
				Hashtag hashTag = Hashtag.builder()
					.content(hashTags)
					.reservationArticle(reservationArticle)
					.build();

				hashtagRepository.save(hashTag);
			}
		}

		// changeReservations 가 존재한다면 아래 로직
		if (updateReservationArticleRequest.changeReservations() != null) {
			List<UpdateReservationRequest> changeReservations = updateReservationArticleRequest.changeReservations();
			List<LocalDateTime> addReservations = new ArrayList<>();
			Map<Long, LocalDateTime> removeReservations = new HashMap<>();

			int addReservationCount = 0;
			int removeReservationCount = 0;
			for (UpdateReservationRequest reservationRequest : changeReservations) {
				if (reservationRequest.changed().equals("add")) {
					addReservationCount++;
					addReservations.add(reservationRequest.startTime());
				} else if (reservationRequest.changed().equals("remove")) {
					removeReservationCount++;
					removeReservations.put(reservationRequest.reservationId(), reservationRequest.startTime());
				}
			}

			// 수정된 날짜들이 10개가 넘는지 체크로직
			long currentReservationCount = reservationRepository.countAllByReservationArticleId(postId);
			long updatedReservationCount = currentReservationCount + addReservationCount - removeReservationCount;
			if (updatedReservationCount >= 10) {
				throw new BusinessException(ReservationArticleErrorCode.TOO_MANY_RESERVATION);
			}

			// 수정된 날짜들이 최대 기간 3일을 넘는지 체크로직
			Map<Long, LocalDateTime> currentReservations = new HashMap<>();
			List<Reservation> currentReservationsList = reservationRepository.findAllByReservationArticleId(postId);
			for (Reservation reservation : currentReservationsList) {
				currentReservations.put(reservation.getId(), reservation.getStartTime());
			}

			if (!currentReservations.isEmpty()) {
				PriorityQueue<LocalDateTime> minHeap = new PriorityQueue<>();
				PriorityQueue<LocalDateTime> maxHeap = new PriorityQueue<>((o1, o2) -> o2.compareTo(o1));

				for (LocalDateTime startTime : currentReservations.values()) {
					minHeap.offer(startTime);
					maxHeap.offer(startTime);
				}

				if (!addReservations.isEmpty()) {
					for (LocalDateTime startTime : addReservations) {
						minHeap.offer(startTime);
						maxHeap.offer(startTime);
					}
				}

				for (LocalDateTime startTime : removeReservations.values()) {
					minHeap.removeIf(dateTime -> dateTime.equals(startTime));
					maxHeap.removeIf(dateTime -> dateTime.equals(startTime));
				}

				LocalDateTime minStartTime = minHeap.poll();
				LocalDateTime maxStartTime = maxHeap.poll();
				long durationInDays = ChronoUnit.DAYS.between(Objects.requireNonNull(minStartTime).toLocalDate(),
					Objects.requireNonNull(maxStartTime).toLocalDate());
				if (durationInDays > 3) {
					throw new BusinessException(ReservationArticleErrorCode.RESERVATION_TIME_LIMIT);
				}
			}

			// 두 체크로직을 통과했다면 add 는 더하고 remove 는 제거 (repository 로 반영)
			removeReservations.forEach((reservationId, startTime) -> reservationRepository.deleteById(reservationId));
			for (LocalDateTime dateTime : addReservations) {
				// 새로운 Chatroom 생성
				ChatRoom chatroom = ChatRoom.builder()
					.roomKey(UUID.randomUUID().toString())
					.build();

				coffeeChatRepository.save(chatroom);

				// Reservation 생성 및 설정
				Reservation reservation = Reservation.builder()
					.startTime(dateTime)
					.endTime(dateTime.plusMinutes(30))
					.reservationArticle(reservationArticle)
					.chatRoom(chatroom)
					.build();

				reservationRepository.save(reservation);
			}
		}
	}

	@Transactional
	public void deleteReservationArticle(Long postId) {
		reservationArticleRepository.findById(postId)
			.orElseThrow(() -> new BusinessException(ReservationArticleErrorCode.RESERVATION_ARTICLE_NOT_FOUND));

		reservationArticleRepository.deleteById(postId);

		coffeeChatRepository.deleteChatRoom(postId);

		reservationRepository.deleteAllByReservationArticleId(postId);

		hashtagRepository.deleteAllByReservationArticleId(postId);
	}

}

