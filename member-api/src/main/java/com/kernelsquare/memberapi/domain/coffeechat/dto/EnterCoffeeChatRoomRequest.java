package com.kernelsquare.memberapi.domain.coffeechat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record EnterCoffeeChatRoomRequest(
	@NotNull(message = "예약 ID를 입력해 주세요.")
	Long reservationId,
	@NotBlank(message = "예약창 제목을 입력해 주세요.")
	String articleTitle
) {
}
