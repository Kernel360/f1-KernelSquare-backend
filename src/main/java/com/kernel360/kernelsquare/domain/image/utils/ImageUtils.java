package com.kernel360.kernelsquare.domain.image.utils;

import com.kernel360.kernelsquare.domain.image.component.ImageComponent;
import org.springframework.web.multipart.MultipartFile;

public class ImageUtils {

    public static String makeFilePath(String category, MultipartFile multipartFile) {
        return category + "/" + multipartFile.getOriginalFilename();
    }

    public static String parseFilePath(String url) {
        if (url != null) {
            String baseUrl = ImageComponent.getBaseUrl();
            return url.replaceFirst(baseUrl + "/", "");
        } else {
            return null;
        }
    }

    public static String makeImageUrl(String filePath) {
        if (filePath != null) {
            String baseUrl = ImageComponent.getBaseUrl();
            return baseUrl + "/" + filePath;
        } else {
            return null;
        }
    }
}
