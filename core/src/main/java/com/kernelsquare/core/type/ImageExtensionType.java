package com.kernelsquare.core.type;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public enum ImageExtensionType {
    JPG("jpg"),
    JPEG("jpeg"),
    PNG("png"),
    SVG("svg"),
    GIF("gif");

    private final String extension;

    public String getExtension() {
        return extension;
    }

    public static List<String> getExtensionList() {
        return Arrays.stream(ImageExtensionType.values()).map(ImageExtensionType::getExtension).toList();
    }
}
