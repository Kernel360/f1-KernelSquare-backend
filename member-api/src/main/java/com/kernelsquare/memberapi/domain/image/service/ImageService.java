package com.kernelsquare.memberapi.domain.image.service;

import com.kernelsquare.domains3.domain.image.repository.ImageStore;
import com.kernelsquare.memberapi.domain.image.dto.UploadImageResponse;
import com.kernelsquare.memberapi.domain.image.validation.ImageValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {
	private final ImageStore imageStore;

	public UploadImageResponse uploadImage(String category, MultipartFile multipartFile) {
		ImageValidation.validateCategory(category);

		ImageValidation.validateFileExists(multipartFile);

		ImageValidation.validateFileExtension(multipartFile);

		String imageUrl = imageStore.store(category, multipartFile);

		return UploadImageResponse.from(imageUrl);
	}

	public void deleteImage(String imageUrl) {
		imageStore.delete(imageUrl);
	}
}
