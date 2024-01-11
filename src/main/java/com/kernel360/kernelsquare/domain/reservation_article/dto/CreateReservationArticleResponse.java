package com.kernel360.kernelsquare.domain.reservation_article.dto;

import com.kernel360.kernelsquare.domain.reservation_article.entity.ReservationArticle;

public record CreateReservationArticleResponse(
        Long reservationArticleId
) {
    public static CreateReservationArticleResponse from(ReservationArticle reservationArticle) {
        return new CreateReservationArticleResponse(
                reservationArticle.getId()
        );
    }
}
