package com.kernel360.kernelsquare.domain.reservation_article.dto;

import com.kernel360.kernelsquare.domain.hashtag.entity.HashTag;
import com.kernel360.kernelsquare.domain.image.utils.ImageUtils;
import com.kernel360.kernelsquare.domain.level.entity.Level;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.reservation.dto.ReservationDto;
import com.kernel360.kernelsquare.domain.reservation_article.entity.ReservationArticle;

import java.time.LocalDateTime;
import java.util.List;

public record FindReservationArticleResponse(
        Long articleId,
        Long memberId,
        String nickname,
        String memberImageUrl,
        Long level,
        String levelImageUrl,
        String title,
        String content,
        List<String> hashTagList,
        List<ReservationDto> dataTimes,
        LocalDateTime createdDate,
        LocalDateTime modifiedDate
) {
    public static FindReservationArticleResponse of(
            Member member,
            ReservationArticle article,
            List<ReservationDto> reservationDtos,
            Level level) {
        return new FindReservationArticleResponse(
                article.getId(),
                member.getId(),
                member.getNickname(),
                ImageUtils.makeImageUrl(member.getImageUrl()),
                level.getName(),
                ImageUtils.makeImageUrl(level.getImageUrl()),
                article.getTitle(),
                article.getContent(),
                article.getHashTagList().stream().map(HashTag::getContent).toList(),
                reservationDtos,
                article.getCreatedDate(),
                article.getModifiedDate()
        );

    }
}
