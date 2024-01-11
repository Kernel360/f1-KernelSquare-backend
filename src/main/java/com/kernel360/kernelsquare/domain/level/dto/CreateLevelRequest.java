package com.kernel360.kernelsquare.domain.level.dto;

import com.kernel360.kernelsquare.domain.image.utils.ImageUtils;
import com.kernel360.kernelsquare.domain.level.entity.Level;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateLevelRequest(
	@NotNull(message = "등급 이름는 필수 항목입니다.")
	Long name,
	@NotBlank(message = "등급 이미지는 필수 항목입니다.")
	String imageUrl,
	@NotNull(message = "등급 경험치 상한선은 필수 항목입니다.")
	Long levelUpperLimit
) {

	public static Level toEntity(
		CreateLevelRequest createLevelRequest) {
		return Level.builder()
			.name(createLevelRequest.name())
			.imageUrl(ImageUtils.parseFilePath(createLevelRequest.imageUrl()))
			.levelUpperLimit(createLevelRequest.levelUpperLimit())
			.build();
	}
}
