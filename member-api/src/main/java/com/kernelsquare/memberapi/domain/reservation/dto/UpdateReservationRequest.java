package com.kernelsquare.memberapi.domain.reservation.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateReservationRequest(
	@NotNull(message = "예약 ID를 입력해 주세요.")
	Long reservationId,
	@NotBlank(message = "시간을 선택해주세요.")
	LocalDateTime startTime,
	@NotBlank(message = "상태 값이 필요합니다.")
	String changed
) {
}
