package com.kernelsquare.adminapi.domain.image.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

public class ImageDto {
    @Builder
    public record FindAllRequest(
        @Size(min = 8, max = 8, message = "yyyyMMdd 형식의 8글자로 입력해 주세요.")
        String createdDate
    ) {}

    @Builder
    public record FindAllResponse(
        List<String> imageList
    ) {}
}
