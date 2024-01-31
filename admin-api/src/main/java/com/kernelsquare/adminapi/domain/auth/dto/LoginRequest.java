package com.kernelsquare.adminapi.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record LoginRequest(
	@NotBlank(message = "이메일을 입력해 주세요.")
	@Email(message = "이메일 형식으로 입력해 주세요.")
	@Size(min = 5, max = 40, message = "이메일 길이를 확인해 주세요.")
	String email,

	@NotBlank(message = "비밀번호를 입력해 주세요.")
	@Size(min = 8, max = 16, message = "비밀번호 길이를 확인해 주세요.")
	String password
) {
}
