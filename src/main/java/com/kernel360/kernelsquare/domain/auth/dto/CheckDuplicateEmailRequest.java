package com.kernel360.kernelsquare.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CheckDuplicateEmailRequest(

	@NotBlank(message = "이메일은 필수 입력 항목입니다.")
	@Email(message = "이메일 형식을 확인해 주세요")
	@Size(min = 5, max = 40, message = "이메일 길이를 확인해 주세요")
	String email
) {
}
