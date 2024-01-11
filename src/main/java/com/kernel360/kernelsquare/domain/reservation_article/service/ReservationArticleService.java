package com.kernel360.kernelsquare.domain.reservation_article.service;

import com.kernel360.kernelsquare.domain.authority.entity.Authority;
import com.kernel360.kernelsquare.domain.authority.repository.AuthorityRepository;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.member.repository.MemberRepository;
import com.kernel360.kernelsquare.domain.reservation_article.dto.CreateReservationArticleRequest;
import com.kernel360.kernelsquare.domain.reservation_article.dto.CreateReservationArticleResponse;
import com.kernel360.kernelsquare.domain.reservation_article.entity.ReservationArticle;
import com.kernel360.kernelsquare.domain.reservation_article.repository.ReservationArticleRepository;
import com.kernel360.kernelsquare.global.common_response.error.code.AuthorityErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.code.MemberErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import com.kernel360.kernelsquare.global.domain.AuthorityType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationArticleService {
    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final ReservationArticleRepository reservationArticleRepository;

    @Transactional
    public CreateReservationArticleResponse createReservationArticle(CreateReservationArticleRequest createReservationArticleRequest) {
        Member member = memberRepository.findById(createReservationArticleRequest.memberId())
                .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
        Authority authority = authorityRepository.findAuthorityByAuthorityType(AuthorityType.ROLE_MENTOR)
                .orElseThrow(() -> new BusinessException(AuthorityErrorCode.AUTHORITY_NOT_FOUND));

        if (!authority.getAuthorityType().equals(AuthorityType.ROLE_MENTOR)) {
            throw new BusinessException(AuthorityErrorCode.AUTHORITY_NOT_FOUND);
        }


        ReservationArticle reservationArticle = CreateReservationArticleRequest.toEntity(createReservationArticleRequest, member);
        ReservationArticle saveReservationArticle = reservationArticleRepository.save(reservationArticle);

        return CreateReservationArticleResponse.from(saveReservationArticle);
    }

}
