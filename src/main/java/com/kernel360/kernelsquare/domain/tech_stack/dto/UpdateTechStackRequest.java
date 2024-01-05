package com.kernel360.kernelsquare.domain.tech_stack.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateTechStackRequest(
	@NotBlank(message = "기술스택은 필수 입력사항입니다.")
	String skill
) {
}
