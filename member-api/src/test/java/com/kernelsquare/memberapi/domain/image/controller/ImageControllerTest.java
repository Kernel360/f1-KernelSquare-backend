package com.kernelsquare.memberapi.domain.image.controller;

import static com.kernelsquare.core.common_response.response.code.ImageResponseCode.*;
import static com.kernelsquare.memberapi.config.ApiDocumentUtils.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import com.kernelsquare.memberapi.config.RestDocsControllerTest;
import com.kernelsquare.memberapi.domain.image.dto.UploadImageResponse;
import com.kernelsquare.memberapi.domain.image.service.ImageService;

@DisplayName("이미지 컨트롤러 테스트")
@WithMockUser
@WebMvcTest(ImageController.class)
class ImageControllerTest extends RestDocsControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ImageService imageService;

	@Test
	@DisplayName("이미지 업로드 성공시 200 Ok와 응답 메시지를 반환한다.")
	public void uploadImage() throws Exception {
		//given
		MockMultipartFile file = new MockMultipartFile(
			"file",
			"hello.png",
			MediaType.IMAGE_PNG_VALUE,
			"Hello, World!".getBytes()
		);

		given(imageService.uploadImage(anyString(), any(MultipartFile.class))).willReturn(
			UploadImageResponse.from("http://example.com/hello.png"));

		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.multipart("/api/v1/images")
			.file(file)
			.param("category", "question")
			.with(csrf())
			.contentType(MediaType.MULTIPART_FORM_DATA)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"));

		//then
		resultActions
			.andExpect(status().is(IMAGE_UPLOAD_COMPLETED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("image-upload-completed", getDocumentRequest(), getDocumentResponse(),
				requestParts(
					partWithName("file").description("이미지 파일")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("data.image_url").type(JsonFieldType.STRING).description("이미지 URL")
				)
			));

		//verify
		verify(imageService, times(1)).uploadImage(anyString(), any(MultipartFile.class));
		verifyNoMoreInteractions(imageService);
	}

	@Test
	@DisplayName("이미지 삭제 성공시 200 Ok와 응답 메시지를 반환한다.")
	public void testDeleteImage() throws Exception {
		//given
		String imageUrl = "http://example.com/test.jpg";

		doNothing()
			.when(imageService)
			.deleteImage(imageUrl);

		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/images")
			.param("imageUrl", imageUrl)
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"));

		//then
		resultActions
			.andExpect(status().is(IMAGE_DELETED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("image-deleted", getDocumentResponse(),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지")
				)
			));
		
		//verify
		verify(imageService, times(1)).deleteImage(imageUrl);
		verifyNoMoreInteractions(imageService);
	}
}