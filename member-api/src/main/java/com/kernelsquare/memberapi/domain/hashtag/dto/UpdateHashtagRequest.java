package com.kernelsquare.memberapi.domain.hashtag.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateHashtagRequest(
	Long hashtagId,
	@NotBlank(message = "내용을 필요합니다.")
	String content,
	@NotBlank(message = "상태 값이 필요합니다.")
	String changed
) {
}