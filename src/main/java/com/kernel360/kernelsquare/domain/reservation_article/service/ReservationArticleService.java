package com.kernel360.kernelsquare.domain.reservation_article.service;

import com.kernel360.kernelsquare.domain.coffeechat.entity.ChatRoom;
import com.kernel360.kernelsquare.domain.coffeechat.repository.CoffeeChatRepository;
import com.kernel360.kernelsquare.domain.hashtag.dto.FindHashtagResponse;
import com.kernel360.kernelsquare.domain.hashtag.entity.Hashtag;
import com.kernel360.kernelsquare.domain.hashtag.repository.HashtagRepository;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.member.repository.MemberRepository;
import com.kernel360.kernelsquare.domain.reservation.dto.FindReservationResponse;
import com.kernel360.kernelsquare.domain.reservation.entity.Reservation;
import com.kernel360.kernelsquare.domain.reservation.repository.ReservationRepository;
import com.kernel360.kernelsquare.domain.reservation_article.dto.CreateReservationArticleRequest;
import com.kernel360.kernelsquare.domain.reservation_article.dto.CreateReservationArticleResponse;
import com.kernel360.kernelsquare.domain.reservation_article.dto.FindAllReservationArticleResponse;
import com.kernel360.kernelsquare.domain.reservation_article.dto.FindReservationArticleResponse;
import com.kernel360.kernelsquare.domain.reservation_article.entity.ReservationArticle;
import com.kernel360.kernelsquare.domain.reservation_article.repository.ReservationArticleRepository;
import com.kernel360.kernelsquare.global.common_response.error.code.MemberErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.code.ReservationArticleErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import com.kernel360.kernelsquare.global.dto.PageResponse;
import com.kernel360.kernelsquare.global.dto.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ReservationArticleService {
    private final MemberRepository memberRepository;
    private final ReservationArticleRepository reservationArticleRepository;
    private final ReservationRepository reservationRepository;
    private final CoffeeChatRepository coffeeChatRepository;
    private final HashtagRepository hashTagRepository;

    @Transactional
    public CreateReservationArticleResponse createReservationArticle(CreateReservationArticleRequest createReservationArticleRequest) {
        Member member = memberRepository.findById(createReservationArticleRequest.memberId())
                .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

        ReservationArticle reservationArticle = CreateReservationArticleRequest.toEntity(createReservationArticleRequest, member);
        ReservationArticle saveReservationArticle = reservationArticleRepository.save(reservationArticle);

        // HashTag 저장
        for (String hashTags : createReservationArticleRequest.hashTags()) {
            Hashtag hashTag = Hashtag.builder()
                    .content(hashTags)
                    .reservationArticle(saveReservationArticle)
                    .build();

            hashTagRepository.save(hashTag);
        }

        for (LocalDateTime dateTime : createReservationArticleRequest.dateTimes()) {
            // 새로운 Chatroom 생성
            ChatRoom chatroom = ChatRoom.builder()
                    .roomKey(UUID.randomUUID().toString())
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

        Integer currentPage = pageable.getPageNumber()+1;
        Page<ReservationArticle> pages = reservationArticleRepository.findAll(pageable);
        Integer totalPages = pages.getTotalPages();

        if (totalPages == 0) totalPages += 1;

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

        List<Hashtag> hashtags = hashTagRepository.findAllByReservationArticleId(postId);
        List<FindHashtagResponse> findHashtagResponses = hashtags.stream()
                .map(FindHashtagResponse::from)
                .toList();


        List<Reservation> reservations = reservationRepository.findAllByReservationArticleId(postId);
        List<FindReservationResponse> findReservationResponses = reservations.stream()
                .map(FindReservationResponse::from)
                .toList();


        return FindReservationArticleResponse.of(member, reservationArticle, findHashtagResponses,findReservationResponses, member.getLevel());
    }

    // TODO 수정하기 로직 여기서 작업
    @Transactional
    public void updateReservationArticle(Long postId) {

    }


    @Transactional
    public void deleteReservationArticle(Long postId) {
        reservationArticleRepository.findById(postId).orElseThrow(() -> new BusinessException(ReservationArticleErrorCode.RESERVATION_ARTICLE_NOT_FOUND));

        reservationArticleRepository.deleteById(postId);

        coffeeChatRepository.deleteChatRoom(postId);

        reservationRepository.deleteAllByReservationArticleId(postId);

        hashTagRepository.deleteAllByReservationArticleId(postId);
    }

}
