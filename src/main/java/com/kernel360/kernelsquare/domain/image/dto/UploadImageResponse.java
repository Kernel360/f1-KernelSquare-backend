package com.kernel360.kernelsquare.domain.image.dto;

public record UploadImageResponse(
    String imageUrl
) {
    public static UploadImageResponse from(String imageUrl) {
        return new UploadImageResponse(imageUrl);
    }
}
