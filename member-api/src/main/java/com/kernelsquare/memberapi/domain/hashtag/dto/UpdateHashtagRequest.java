package com.kernelsquare.memberapi.domain.hashtag.dto;

import com.kernelsquare.core.validation.annotations.BadWordFilter;
import com.kernelsquare.core.validation.constants.BadWordValidationMessage;

import jakarta.validation.constraints.NotBlank;

public record UpdateHashtagRequest(
	Long hashtagId,
	@NotBlank(message = "내용을 필요합니다.")
	@BadWordFilter(message = BadWordValidationMessage.NO_BAD_WORD_IN_CONTENT)
	String content,
	@NotBlank(message = "상태 값이 필요합니다.")
	String changed
) {
}