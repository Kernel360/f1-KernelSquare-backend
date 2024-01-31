package com.kernelsquare.memberapi.domain.image.controller;

import static com.kernelsquare.core.common_response.response.code.ImageResponseCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kernelsquare.memberapi.domain.image.dto.UploadImageResponse;
import com.kernelsquare.memberapi.domain.image.service.ImageService;
import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ImageController {
	private final ImageService imageService;

	@PostMapping("/images")
	public ResponseEntity<ApiResponse<UploadImageResponse>> uploadImage(
		@RequestParam
		String category,
		@RequestPart(value = "file")
		MultipartFile multipartFile
	) {
		UploadImageResponse uploadImageResponse = imageService.uploadImage(category, multipartFile);

		return ResponseEntityFactory.toResponseEntity(IMAGE_UPLOAD_COMPLETED, uploadImageResponse);
	}

	@DeleteMapping("/images")
	public ResponseEntity<ApiResponse> deleteImage(
		@RequestParam String imageUrl
	) {
		imageService.deleteImage(imageUrl);

		return ResponseEntityFactory.toResponseEntity(IMAGE_DELETED);
	}
}
