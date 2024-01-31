package com.kernelsquare.adminapi.domain.tech_stack.dto;

import java.util.List;

import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;

public record FindAllTechStacksResponse(
	List<TechStack> skills
) {
	public static FindAllTechStacksResponse from(List<TechStack> skills) {
		return new FindAllTechStacksResponse(
			skills
		);
	}
}
