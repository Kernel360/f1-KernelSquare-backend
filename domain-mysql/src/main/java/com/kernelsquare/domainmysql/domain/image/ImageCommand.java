package com.kernelsquare.domainmysql.domain.image;

import lombok.Builder;

import java.time.LocalDate;

public class ImageCommand {
    @Builder
    public record FindAllImages(
        LocalDate createdDate
    ) {}
}
