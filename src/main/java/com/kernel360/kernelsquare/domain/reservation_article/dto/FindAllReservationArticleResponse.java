package com.kernel360.kernelsquare.domain.reservation_article.dto;

import com.kernel360.kernelsquare.domain.hashtag.entity.HashTag;
import com.kernel360.kernelsquare.domain.image.utils.ImageUtils;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.reservation_article.entity.ReservationArticle;

import java.time.LocalDateTime;
import java.util.List;


public record FindAllReservationArticleResponse(
        Long articleId,
        Long memberId,
        String nickname,
        String memberImageUrl,
        Long level,
        String levelImageUrl,
        String title,
        List<String> hashTagList,
        LocalDateTime createdDate,
        LocalDateTime modifiedDate,
        Long fullCheck
) {
    public static FindAllReservationArticleResponse of(
            Member member,
            ReservationArticle article,
            Long fullCheck) {
        return new FindAllReservationArticleResponse(
                article.getId(),
                member.getId(),
                member.getNickname(),
                ImageUtils.makeImageUrl(member.getImageUrl()),
                member.getLevel().getName(),
                ImageUtils.makeImageUrl(member.getLevel().getImageUrl()),
                article.getTitle(),
                article.getHashTagList().stream().map(HashTag::getContent).toList(),
                article.getCreatedDate(),
                article.getModifiedDate(),
                fullCheck
        );
    }
}