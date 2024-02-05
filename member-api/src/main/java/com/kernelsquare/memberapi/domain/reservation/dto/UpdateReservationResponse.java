package com.kernelsquare.memberapi.domain.reservation.dto;

import com.kernelsquare.domainmysql.domain.reservation.entity.Reservation;

import lombok.Builder;

@Builder
public record UpdateReservationResponse(
	Long reservationId
) {
	public static UpdateReservationResponse from(Reservation reservation) {
		return UpdateReservationResponse.builder().reservationId(reservation.getId()).build();
	}
}
