package com.kernelsquare.memberapi.domain.reservation.service;

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
	LocalDateTime startTime,
	String roomKey
) {

	public static Reservation toEntity(AddReservationMemberRequest addReservationMemberRequest,
		ReservationArticle reservationArticle,
		ChatRoom chatRoom) {
		return Reservation.builder()
			.startTime(addReservationMemberRequest.startTime())
			.endTime(addReservationMemberRequest.startTime().plusMinutes(30L))
			.reservationArticle(reservationArticle)
			.chatRoom(chatRoom)
			.build();
	}
}
