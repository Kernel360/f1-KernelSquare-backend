package com.kernel360.kernelsquare.domain.reservation.dto;

import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.reservation.entity.Reservation;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Optional;

@Builder
public record FindReservationResponse(
        Long reservationId,
        Long roomId,
        LocalDateTime startTime,
        String mentiNickname,
        String mentiImageUrl
)  {
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