package com.kernel360.kernelsquare.domain.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.kernel360.kernelsquare.domain.image.dto.CategoryDto;
import com.kernel360.kernelsquare.domain.image.dto.UploadImageResponse;
import com.kernel360.kernelsquare.domain.image.utils.ImageUtils;
import com.kernel360.kernelsquare.global.common_response.error.code.CategoryErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.code.ImageErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final AmazonS3Client amazonS3Client;
    private static final List<String> CATEGORYLIST = CategoryDto.getCategoryList();

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public UploadImageResponse uploadImage(String category, MultipartFile multipartFile) {
        validateCategory(category);

        validateFileExists(multipartFile);

        String filePath = ImageUtils.makeFilePath(category, multipartFile);

        ObjectMetadata metadata= new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());

        try {
            amazonS3Client.putObject(bucket, filePath, multipartFile.getInputStream(), metadata);

            UploadImageResponse uploadImageResponse = UploadImageResponse.from(amazonS3Client.getUrl(bucket, filePath).toString());

            return uploadImageResponse;
        } catch (IOException e) {
            throw new BusinessException(ImageErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }

    public void deleteImage(String imageUrl) {
        String keyName = ImageUtils.parseFilePath(imageUrl);

        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, keyName));
    }

    private void validateFileExists(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new BusinessException(ImageErrorCode.IMAGE_IS_EMPTY);
        }
    }

    private void validateCategory(String category) {
        if (!CATEGORYLIST.contains(category)) {
            throw new BusinessException(CategoryErrorCode.CATEGORY_NOT_VALID);
        }
    }
}
