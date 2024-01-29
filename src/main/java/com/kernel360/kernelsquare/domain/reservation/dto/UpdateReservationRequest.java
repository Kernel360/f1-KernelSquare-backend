package com.kernel360.kernelsquare.domain.reservation.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record UpdateReservationRequest(
        Long reservationId,
        @NotBlank(message = "시간을 선택해주세요.")
        LocalDateTime startTime,
        @NotBlank(message = "add 혹은 remove 가 필요합니다.")
        String changed
) {
}
