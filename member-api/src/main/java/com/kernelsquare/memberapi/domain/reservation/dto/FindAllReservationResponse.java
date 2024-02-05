package com.kernelsquare.memberapi.domain.reservation.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record FindAllReservationResponse(
	List<FindReservationResponse> reservationResponses
) {
	public static FindAllReservationResponse from(List<FindReservationResponse> reservationResponses) {
		return FindAllReservationResponse
			.builder()
			.reservationResponses(reservationResponses)
			.build();
	}
}
