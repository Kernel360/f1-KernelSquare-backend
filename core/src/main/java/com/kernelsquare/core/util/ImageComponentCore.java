package com.kernelsquare.core.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
public class ImageComponentCore {
    @Getter
    private static String baseUrl;

    @Value("${custom.domain.image.base-url}")
    public void setBaseUrl(String baseUrl) {
        ImageComponentCore.baseUrl = baseUrl;
    }

}
