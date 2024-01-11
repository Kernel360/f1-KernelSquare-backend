package com.kernel360.kernelsquare.domain.level.dto;

import com.kernel360.kernelsquare.domain.image.utils.ImageUtils;
import com.kernel360.kernelsquare.domain.level.entity.Level;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link com.kernel360.kernelsquare.domain.level.entity.Level}
 */
public record UpdateLevelRequest(
	@NotNull(message = "등급은 필수 입력사항입니다.")
	Long name,
	@NotBlank(message = "등급 이미지 URL은 필수 입력사항입니다.")
	String imageUrl,
	@NotNull(message = "등급 경험치 상한선은 필수 입력사항입니다.")
	Long levelUpperLimit
) {}