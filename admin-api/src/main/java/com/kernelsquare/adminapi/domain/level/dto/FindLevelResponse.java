package com.kernelsquare.adminapi.domain.level.dto;

import com.kernelsquare.core.util.ImageUtils;
import com.kernelsquare.domainmysql.domain.level.entity.Level;

public record FindLevelResponse(Long id, Long name, String imageUrl, Long leveUpperLimit) {
	public static FindLevelResponse from(Level level) {
		return new FindLevelResponse(level.getId(), level.getName(), ImageUtils.makeImageUrl(level.getImageUrl()),
			level.getLevelUpperLimit());
	}
}