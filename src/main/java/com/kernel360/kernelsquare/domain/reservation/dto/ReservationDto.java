package com.kernel360.kernelsquare.domain.reservation.dto;

import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.reservation.entity.Reservation;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReservationDto(
        @NotNull LocalDateTime startTime,
        @NotNull Boolean finished,
        String memberNickname,
        String memberImageUrl
)  {
    public static ReservationDto of(Reservation reservation, Member member) {
        return new ReservationDto(
                reservation.getStartTime(),
                reservation.getFinished(),
                member.getNickname(),
                member.getImageUrl()
        );
    }
}