package com.kernelsquare.adminapi.domain.level.dto;

import com.kernelsquare.core.util.ImageUtils;
import com.kernelsquare.domainmysql.domain.level.entity.Level;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateLevelRequest(
	@NotNull(message = "등급 이름을 입력해 주세요.")
	Long name,
	@NotBlank(message = "등급 이미지 URL을 입력해 주세요.")
	String imageUrl,
	@NotNull(message = "등급 경험치 상한선을 입력해 주세요.")
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
