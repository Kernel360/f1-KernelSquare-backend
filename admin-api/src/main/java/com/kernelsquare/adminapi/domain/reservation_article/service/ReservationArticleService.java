package com.kernelsquare.adminapi.domain.reservation_article.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kernelsquare.adminapi.domain.reservation.dto.FindReservationResponse;
import com.kernelsquare.adminapi.domain.reservation_article.dto.FindAllReservationArticleResponse;
import com.kernelsquare.adminapi.domain.reservation_article.dto.FindReservationArticleResponse;
import com.kernelsquare.core.common_response.error.code.ReservationArticleErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.core.dto.Pagination;
import com.kernelsquare.domainmysql.domain.coffeechat.repository.CoffeeChatRepository;
import com.kernelsquare.domainmysql.domain.hashtag.repository.HashtagRepository;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;
import com.kernelsquare.domainmysql.domain.reservation.repository.ReservationRepository;
import com.kernelsquare.domainmysql.domain.reservation_article.entity.ReservationArticle;
import com.kernelsquare.domainmysql.domain.reservation_article.repository.ReservationArticleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationArticleService {
	private final MemberRepository memberRepository;
	private final ReservationArticleRepository reservationArticleRepository;
	private final ReservationRepository reservationRepository;
	private final CoffeeChatRepository coffeeChatRepository;
	private final HashtagRepository hashTagRepository;

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
		List<FindReservationResponse> reservationList = reservationRepository
			.findAllByReservationArticleId(postId)
			.stream()
			.map(FindReservationResponse::from)
			.toList();

		return FindReservationArticleResponse.of(member, reservationArticle, reservationList, member.getLevel());
	}

	@Transactional
	public void deleteReservationArticle(Long postId) {
		reservationArticleRepository.findById(postId)
			.orElseThrow(() -> new BusinessException(ReservationArticleErrorCode.RESERVATION_ARTICLE_NOT_FOUND));

		reservationArticleRepository.deleteById(postId);

		coffeeChatRepository.deleteChatRoom(postId);

		reservationRepository.deleteAllByReservationArticleId(postId);

		hashTagRepository.deleteAllByReservationArticleId(postId);
	}
}
