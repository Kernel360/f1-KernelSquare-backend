package com.kernel360.kernelsquare.domain.tech_stack.dto;

import com.kernel360.kernelsquare.domain.tech_stack.entity.TechStack;

import jakarta.validation.constraints.NotBlank;

public record CreateTechStackRequest(
	@NotBlank(message = "기술스택은 필수 입력사항입니다.")
	String skill
) {
	public static TechStack toEntity(CreateTechStackRequest createTechStackRequest) {
		return TechStack.builder()
			.skill(createTechStackRequest.skill())
			.build();
	}
}
