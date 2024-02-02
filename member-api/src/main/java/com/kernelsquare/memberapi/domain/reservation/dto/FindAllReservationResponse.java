package com.kernelsquare.memberapi.domain.reservation.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record FindAllReservationResponse(
	List<FindReservationResponse> reservationResponseList
) {
	public static FindAllReservationResponse from(List<FindReservationResponse> reservationResponseList) {
		return FindAllReservationResponse
			.builder()
			.reservationResponseList(reservationResponseList)
			.build();
	}
}
