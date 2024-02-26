package com.kernelsquare.memberapi.domain.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.kernelsquare.core.common_response.error.code.ImageErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.memberapi.domain.image.validation.ImageValidation;
import com.kernelsquare.memberapi.domain.image.dto.UploadImageResponse;
import com.kernelsquare.core.util.ImageUtils;
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

	public UploadImageResponse uploadImage(String category, MultipartFile multipartFile) {
		ImageValidation.validateCategory(category);

		ImageValidation.validateFileExists(multipartFile);

		ImageValidation.validateFileExtension(multipartFile);

		String filePath = ImageUtils.makeFilePath(category, multipartFile);

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(multipartFile.getContentType());
		metadata.setContentLength(multipartFile.getSize());

		try {
			amazonS3Client.putObject(bucket, filePath, multipartFile.getInputStream(), metadata);

			UploadImageResponse uploadImageResponse = UploadImageResponse.from(
				amazonS3Client.getUrl(bucket, filePath).toString());

			return uploadImageResponse;
		} catch (IOException e) {
			throw new BusinessException(ImageErrorCode.IMAGE_UPLOAD_FAILED);
		}
	}

	public void deleteImage(String imageUrl) {
		String keyName = ImageUtils.parseFilePath(imageUrl);

		amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, keyName));
	}
}
