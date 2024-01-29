package com.kernel360.kernelsquare.domain.hashtag.dto;

import jakarta.validation.constraints.NotBlank;


public record UpdateHashtagRequest(
        Long hashtagId,
        @NotBlank(message = "내용을 필요합니다.")
        String content,
        @NotBlank(message = "add 혹은 remove 가 필요합니다.")
        String changed
) {
}