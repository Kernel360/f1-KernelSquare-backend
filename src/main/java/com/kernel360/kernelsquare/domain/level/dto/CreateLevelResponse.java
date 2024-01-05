package com.kernel360.kernelsquare.domain.level.dto;

import com.kernel360.kernelsquare.domain.level.entity.Level;

public record CreateLevelResponse(
	Long levelId
) {
	public static CreateLevelResponse from(Level level) {
		return new CreateLevelResponse(
			level.getId()
		);
	}

}