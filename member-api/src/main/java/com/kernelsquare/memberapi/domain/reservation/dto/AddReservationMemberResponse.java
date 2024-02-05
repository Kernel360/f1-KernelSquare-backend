package com.kernelsquare.memberapi.domain.reservation.dto;

import com.kernelsquare.domainmysql.domain.reservation.entity.Reservation;

import lombok.Builder;

@Builder
public record AddReservationMemberResponse(
	Long reservationArticleId
) {

	public static AddReservationMemberResponse of(Reservation reservation) {
		return AddReservationMemberResponse.builder()
			.reservationArticleId(reservation.getReservationArticle().getId())
			.build();
	}
}
