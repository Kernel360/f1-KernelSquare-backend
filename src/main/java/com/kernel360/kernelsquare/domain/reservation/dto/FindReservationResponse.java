package com.kernel360.kernelsquare.domain.reservation.dto;

import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.reservation.entity.Reservation;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FindReservationResponse(
        Long reservationId,
        LocalDateTime startTime,
        String mentiNickname,
        String mentiImageUrl
)  {
    public static FindReservationResponse from(Reservation reservation) {
        return new FindReservationResponse(
                reservation.getId(),
                reservation.getStartTime(),
                reservation.getMember().getNickname(),
                reservation.getMember().getImageUrl()
        );
    }
}