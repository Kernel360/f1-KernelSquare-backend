package com.kernelsquare.adminapi.domain.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.kernelsquare.adminapi.domain.image.dto.ImageCategory;
import com.kernelsquare.adminapi.domain.image.dto.UploadImageResponse;
import com.kernelsquare.core.common_response.error.code.CategoryErrorCode;
import com.kernelsquare.core.common_response.error.code.ImageErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.util.ImageUtils;
import com.kernelsquare.domainmysql.domain.image.command.ImageCommand;
import com.kernelsquare.domainmysql.domain.image.info.ImageInfo;
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
	private static final List<String> CATEGORYLIST = ImageCategory.getCategoryList();

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	public UploadImageResponse uploadImage(String category, MultipartFile multipartFile) {
		validateCategory(category);

		validateFileExists(multipartFile);

		String filePath = ImageUtils.makeFilePath(category);

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

	public ImageInfo findAllImages(ImageCommand.FindAllImages command) {
		String prefix = command.createdDate() + "/";

		ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request()
			.withBucketName(bucket)
			.withPrefix(prefix);

		ListObjectsV2Result listObjectsV2Result = amazonS3Client.listObjectsV2(listObjectsV2Request);

		List<S3ObjectSummary> objectSummaries = listObjectsV2Result.getObjectSummaries();

		List<String> imageList = objectSummaries.stream()
			.map(objectSummary -> {
				String key = objectSummary.getKey();
				S3Object object = amazonS3Client.getObject(bucket, key);
				return ImageUtils.makeImageUrl(object.getKey());
			})
			.toList();

		return ImageInfo.from(imageList);
	}
}
