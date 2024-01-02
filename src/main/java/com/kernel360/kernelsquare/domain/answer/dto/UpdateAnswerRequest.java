package com.kernel360.kernelsquare.domain.answer.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateAnswerRequest (
    @NotBlank
    String content,
    String imageUrl
) {}