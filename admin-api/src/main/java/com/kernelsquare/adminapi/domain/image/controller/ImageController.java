package com.kernelsquare.adminapi.domain.image.controller;

import static com.kernelsquare.core.common_response.response.code.ImageResponseCode.*;

import com.kernelsquare.adminapi.domain.image.dto.ImageDto;
import com.kernelsquare.adminapi.domain.image.facade.ImageFacade;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.kernelsquare.adminapi.domain.image.dto.UploadImageResponse;
import com.kernelsquare.adminapi.domain.image.service.ImageService;
import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ImageController {
	private final ImageService imageService;
	private final ImageFacade imageFacade;

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

	@GetMapping("/images")
	public ResponseEntity<ApiResponse<ImageDto.FindAllResponse>> findAllImages(
		@Valid
		@RequestBody
		ImageDto.FindAllRequest request
	) {
		ImageDto.FindAllResponse response = imageFacade.findAllImages(request);
		return ResponseEntityFactory.toResponseEntity(IMAGE_ALL_FOUND, response);
	}
}
