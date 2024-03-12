package com.kernelsquare.domainmysql.domain.image.command;

import lombok.Builder;

public class ImageCommand {
    @Builder
    public record FindAllImages(
        String createdDate
    ) {}
}
