package com.kernelsquare.adminapi.domain.reservation.dto;

import java.time.LocalDateTime;

import com.kernelsquare.domainmysql.domain.reservation.entity.Reservation;

import lombok.Builder;

@Builder
public record FindReservationResponse(
	Long reservationId,
	Long roomId,
	LocalDateTime startTime,
	String mentiNickname,
	String mentiImageUrl
) {
	public static FindReservationResponse from(Reservation reservation) {

		String nickname = null;
		String imageUrl = null;

		if (reservation.getMember() != null) {
			nickname = reservation.getMember().getNickname();
			imageUrl = reservation.getMember().getImageUrl();
		}

		return new FindReservationResponse(
			reservation.getId(),
			reservation.getChatRoom().getId(),
			reservation.getStartTime(),
			nickname,
			imageUrl
		);
	}
}