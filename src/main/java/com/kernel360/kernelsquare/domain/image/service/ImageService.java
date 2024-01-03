package com.kernel360.kernelsquare.domain.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.kernel360.kernelsquare.global.common_response.error.code.ImageErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImage(String category, MultipartFile multipartFile) {
        validateFileExists(multipartFile);

        String fileName = category + "/" + multipartFile.getOriginalFilename();

        ObjectMetadata metadata= new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());

        try {
            amazonS3Client.putObject(bucket,fileName,multipartFile.getInputStream(),metadata);
            String fileUrl= amazonS3Client.getUrl(bucket, fileName).toString();

            switch (category) {
                case "m" -> {}
                case "q" -> {}
                case "a" -> {}
                default -> throw new IllegalArgumentException("Invalid category: " + category);
            }

            return fileUrl;
        } catch (IOException e) {
            throw new BusinessException(ImageErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }

    private void validateFileExists(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new BusinessException(ImageErrorCode.IMAGE_IS_EMPTY);
        }
    }
}
