package com.kernel360.kernelsquare.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CheckDuplicateNicknameRequest(
	@NotBlank(message = "닉네임은 필수 입력 항목입니다.")
	@Size(min = 2, max = 8, message = "닉네임 길이를 확인해 주세요")
	String nickname
) {
}
