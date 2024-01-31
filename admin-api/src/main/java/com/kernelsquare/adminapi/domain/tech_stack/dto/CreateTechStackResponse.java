package com.kernelsquare.adminapi.domain.tech_stack.dto;

import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;

public record CreateTechStackResponse(
	Long skillId
) {
	public static CreateTechStackResponse from(TechStack techStack) {
		return new CreateTechStackResponse(
			techStack.getId()
		);
	}
}
