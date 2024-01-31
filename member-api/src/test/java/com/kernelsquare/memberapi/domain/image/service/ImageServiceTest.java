package com.kernelsquare.memberapi.domain.image.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.kernelsquare.memberapi.domain.image.dto.UploadImageResponse;

@DisplayName("이미지 서비스 통합 테스트")
@ExtendWith(MockitoExtension.class)
class ImageServiceTest {
	@InjectMocks
	private ImageService imageService;
	@Mock
	private AmazonS3Client amazonS3Client;

	@Test
	@DisplayName("이미지 업로드 테스트")
	void testUploadImage() throws MalformedURLException {
		// given
		String category = "question";

		MockMultipartFile file = new MockMultipartFile(
			"file",
			"hello.png",
			MediaType.IMAGE_PNG_VALUE,
			"Hello, World!".getBytes()
		);

		PutObjectResult mockPutObjectResult = new PutObjectResult();

		URL mockUrl = new URL("https://test-bucket.s3.amazonaws.com/hello.png");

		given(
			amazonS3Client.putObject(any(), anyString(), any(InputStream.class), any(ObjectMetadata.class))).willReturn(
			mockPutObjectResult);
		given(amazonS3Client.getUrl(any(), anyString())).willReturn(mockUrl);

		// when
		UploadImageResponse uploadImageResponse = imageService.uploadImage(category, file);

		// then
		assertThat(uploadImageResponse.imageUrl()).isEqualTo(mockUrl.toString());

		//verify
		verify(amazonS3Client, times(1)).putObject(any(), anyString(), any(InputStream.class),
			any(ObjectMetadata.class));
		verify(amazonS3Client, times(1)).getUrl(any(), anyString());
	}

	@Test
	@DisplayName("이미지 삭제 테스트")
	void deleteImage_ValidImageUrl_DeletesImage() {
		// given
		String imageUrl = "https://test-bucket.s3.amazonaws.com/images/image.jpg";

		// when
		imageService.deleteImage(imageUrl);

		// then
		verify(amazonS3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
	}
}