package com.kernelsquare.adminapi.domain.image.dto;

import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class ImageDto {
    @Builder
    public record FindAllRequest(
        @DateTimeFormat(pattern = "yyyyMMdd")
        LocalDate createdDate
    ) {}
}
