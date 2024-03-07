package com.kernelsquare.memberapi.domain.reservation.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kernelsquare.core.constants.TimeResponseFormat;
import com.kernelsquare.domainmysql.domain.reservation.entity.Reservation;

import com.kernelsquare.core.util.ImageUtils;
import lombok.Builder;

@Builder
public record FindReservationResponse(
	Long reservationId,
	Long roomId,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
	LocalDateTime startTime,
	String menteeNickname,
	String menteeImageUrl
) {
	public static FindReservationResponse from(Reservation reservation) {

		String nickname = null;
		String imageUrl = null;

		if (reservation.getMember() != null) {
			nickname = reservation.getMember().getNickname();
			imageUrl = ImageUtils.makeImageUrl(reservation.getMember().getImageUrl());
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