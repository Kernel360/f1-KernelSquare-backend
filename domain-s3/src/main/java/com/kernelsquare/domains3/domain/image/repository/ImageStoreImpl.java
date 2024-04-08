package com.kernelsquare.domains3.domain.image.repository;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.kernelsquare.core.common_response.error.code.ImageErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ImageStoreImpl implements ImageStore {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String store(String category, MultipartFile multipartFile) {
        String filePath = ImageUtils.makeFilePath(category, multipartFile);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());

        try {
            amazonS3Client.putObject(bucket, filePath, multipartFile.getInputStream(), metadata);

            String ImageUrl = amazonS3Client.getUrl(bucket, filePath).toString();

            return ImageUrl;
        } catch (IOException e) {
            throw new BusinessException(ImageErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }

    @Override
    public void delete(String imageUrl) {
        String keyName = ImageUtils.parseFilePath(imageUrl);

        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, keyName));
    }
}
