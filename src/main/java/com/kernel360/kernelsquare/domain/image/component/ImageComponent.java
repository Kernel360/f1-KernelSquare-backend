package com.kernel360.kernelsquare.domain.image.component;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ImageComponent {
    @Getter
    private static String baseUrl;

    @Value("${custom.domain.image.base-url}")
    public void setBaseUrl(String baseUrl) {
        ImageComponent.baseUrl = baseUrl;
    }

}
