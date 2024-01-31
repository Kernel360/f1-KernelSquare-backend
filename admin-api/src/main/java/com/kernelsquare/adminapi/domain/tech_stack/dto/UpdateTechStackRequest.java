package com.kernelsquare.adminapi.domain.tech_stack.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateTechStackRequest(
	@NotBlank(message = "기술 스택을 입력해 주세요.")
	String skill
) {
}
