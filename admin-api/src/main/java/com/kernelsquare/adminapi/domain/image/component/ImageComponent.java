package com.kernelsquare.adminapi.domain.image.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
public class ImageComponent {
    @Getter
    private static String baseUrl;

    @Value("${custom.domain.image.base-url}")
    public void setBaseUrl(String baseUrl) {
        ImageComponent.baseUrl = baseUrl;
    }

}
