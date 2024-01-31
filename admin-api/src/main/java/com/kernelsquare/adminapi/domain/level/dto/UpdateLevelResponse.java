package com.kernelsquare.adminapi.domain.level.dto;

import com.kernelsquare.domainmysql.domain.level.entity.Level;

public record UpdateLevelResponse(
	Long id,
	Long name,
	String imageUrl,
	Long levelUpperLimit
) {
	public static UpdateLevelResponse from(Level level) {
		return new UpdateLevelResponse(
			level.getId(),
			level.getName(),
			level.getImageUrl(),
			level.getLevelUpperLimit()
		);
	}
}