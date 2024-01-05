package com.kernel360.kernelsquare.domain.level.dto;

import com.kernel360.kernelsquare.domain.level.entity.Level;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateLevelRequest(
        Long name,
        String imageUrl
) {

	public static Level toEntity(
		CreateLevelRequest createLevelRequest) {
		return Level.builder()
			.name(createLevelRequest.name())
			.imageUrl(createLevelRequest.imageUrl())
			.build();
	}
}
