package com.kernelsquare.memberapi.domain.reservation_article.dto;

import com.kernelsquare.domainmysql.domain.reservation_article.entity.ReservationArticle;

import lombok.Builder;

@Builder
public record CreateReservationArticleResponse(
	Long reservationArticleId
) {
	public static CreateReservationArticleResponse from(ReservationArticle reservationArticle) {
		return new CreateReservationArticleResponse(
			reservationArticle.getId()
		);
	}
}
