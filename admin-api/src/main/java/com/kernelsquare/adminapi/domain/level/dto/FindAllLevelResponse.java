package com.kernelsquare.adminapi.domain.level.dto;

import java.util.List;

import com.kernelsquare.domainmysql.domain.level.entity.Level;

public record FindAllLevelResponse(
	List<FindLevelResponse> levels
) {

	public static FindAllLevelResponse from(List<Level> levels) {
		return new FindAllLevelResponse(
			levels.stream().map(FindLevelResponse::from).toList()
		);
	}
}
