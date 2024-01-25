package com.kernel360.kernelsquare.domain.hashtag.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record UpdateHashtagRequest(
        Long id,
        @NotBlank
        String content) {

}