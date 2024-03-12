package com.kernelsquare.domainmysql.domain.image.info;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ImageInfo {
    private final List<String> imageList;

    @Builder
    public ImageInfo(List<String> imageList) {
        this.imageList = imageList;
    }

    public static ImageInfo from(List<String> imageList) {
        return ImageInfo.builder()
            .imageList(imageList)
            .build();
    }
}
