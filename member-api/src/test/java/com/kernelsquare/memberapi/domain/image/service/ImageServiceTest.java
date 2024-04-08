package com.kernelsquare.memberapi.domain.image.service;

import com.kernelsquare.domains3.domain.image.repository.ImageStore;
import com.kernelsquare.memberapi.domain.image.dto.UploadImageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

@DisplayName("이미지 서비스 통합 테스트")
@ExtendWith(MockitoExtension.class)
class ImageServiceTest {
	@InjectMocks
	private ImageService imageService;
	@Mock
	private ImageStore imageStore;

	@Test
	@DisplayName("이미지 업로드 테스트")
	void testUploadImage() {
		// given
		String category = "question";

		MockMultipartFile file = new MockMultipartFile(
			"file",
			"hello.png",
			MediaType.IMAGE_PNG_VALUE,
			"Hello, World!".getBytes()
		);

		String imageUrl = "https://test-bucket.s3.amazonaws.com/" + category + "/" + file.getOriginalFilename();

		doReturn(imageUrl)
			.when(imageStore)
			.store(category, file);

		// when
		UploadImageResponse uploadImageResponse = imageService.uploadImage(category, file);

		// then
		assertThat(uploadImageResponse.imageUrl()).isEqualTo(imageUrl);

		//verify
		verify(imageStore, times(1)).store(anyString(), any(MultipartFile.class));
	}

	@Test
	@DisplayName("이미지 삭제 테스트")
	void deleteImage_ValidImageUrl_DeletesImage() {
		// given
		String imageUrl = "https://test-bucket.s3.amazonaws.com/images/image.jpg";

		// when
		imageService.deleteImage(imageUrl);

		// then
		verify(imageStore, times(1)).delete(anyString());
	}
}