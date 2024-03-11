package com.kernelsquare.memberapi.domain.reservation.dto;

import java.time.LocalDateTime;

import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;
import com.kernelsquare.domainmysql.domain.reservation.entity.Reservation;
import com.kernelsquare.domainmysql.domain.reservation_article.entity.ReservationArticle;

import lombok.Builder;

@Builder
public record AddReservationMemberRequest(
	Long reservationArticleId,
	Long reservationId,
	Long memberId,
	LocalDateTime reservationStartTime
) {

	public static Reservation toEntity(AddReservationMemberRequest addReservationMemberRequest,
		ReservationArticle reservationArticle,
		ChatRoom chatRoom) {
		return Reservation.builder()
			.startTime(addReservationMemberRequest.reservationStartTime())
			.endTime(addReservationMemberRequest.reservationStartTime().plusMinutes(30L))
			.reservationArticle(reservationArticle)
			.chatRoom(chatRoom)
			.build();
	}
}
