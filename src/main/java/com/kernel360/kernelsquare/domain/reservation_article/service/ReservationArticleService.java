package com.kernel360.kernelsquare.domain.reservation_article.service;

import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.member.repository.MemberRepository;
import com.kernel360.kernelsquare.domain.reservation.dto.ReservationDto;
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

import java.util.List;


@Service
@RequiredArgsConstructor
public class ReservationArticleService {
    private final MemberRepository memberRepository;
    private final ReservationArticleRepository reservationArticleRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public CreateReservationArticleResponse createReservationArticle(CreateReservationArticleRequest createReservationArticleRequest) {
        Member member = memberRepository.findById(createReservationArticleRequest.memberId())
                .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

        ReservationArticle reservationArticle = CreateReservationArticleRequest.toEntity(createReservationArticleRequest, member);
        ReservationArticle saveReservationArticle = reservationArticleRepository.save(reservationArticle);

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
    public FindReservationArticleResponse findReservationArticle(
            Long postId
    ) {
        ReservationArticle reservationArticle = reservationArticleRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ReservationArticleErrorCode.RESERVATION_ARTICLE_NOT_FOUND));
        Member member = reservationArticle.getMember();
        List<ReservationDto> reservationDTOs = reservationRepository.findAllByReservationArticleId(postId);

        return FindReservationArticleResponse.of(member, reservationArticle, reservationDTOs, member.getLevel());
    }

}
