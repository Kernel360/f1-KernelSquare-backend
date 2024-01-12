package com.kernel360.kernelsquare.domain.level.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * DTO for {@link com.kernel360.kernelsquare.domain.level.entity.Level}
 */
@Builder
public record UpdateLevelRequest(
	@NotNull(message = "등급 이름을 입력해 주세요.")
	Long name,
	@NotBlank(message = "등급 이미지 URL을 입력해 주세요.")
	String imageUrl,
	@NotNull(message = "등급 경험치 상한선을 입력해 주세요.")
	Long levelUpperLimit
) {}