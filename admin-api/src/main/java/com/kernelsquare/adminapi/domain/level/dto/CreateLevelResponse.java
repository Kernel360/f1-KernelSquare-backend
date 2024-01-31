package com.kernelsquare.adminapi.domain.level.dto;

import com.kernelsquare.domainmysql.domain.level.entity.Level;

public record CreateLevelResponse(
	Long levelId
) {
	public static CreateLevelResponse from(Level level) {
		return new CreateLevelResponse(
			level.getId()
		);
	}
}