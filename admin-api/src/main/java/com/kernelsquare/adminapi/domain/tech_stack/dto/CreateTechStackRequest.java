package com.kernelsquare.adminapi.domain.tech_stack.dto;

import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateTechStackRequest(
	@NotBlank(message = "기술 스택을 입력해 주세요.")
	String skill
) {
	public static TechStack toEntity(CreateTechStackRequest createTechStackRequest) {
		return TechStack.builder()
			.skill(createTechStackRequest.skill())
			.build();
	}
}
