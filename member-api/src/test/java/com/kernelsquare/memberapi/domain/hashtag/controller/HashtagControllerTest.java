package com.kernelsquare.memberapi.domain.hashtag.controller;

import static com.kernelsquare.core.common_response.response.code.HashtagResponseCode.HashTagResponseCode.*;
import static com.kernelsquare.memberapi.config.ApiDocumentUtils.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.kernelsquare.domainmysql.domain.hashtag.entity.Hashtag;
import com.kernelsquare.memberapi.config.RestDocsControllerTest;
import com.kernelsquare.memberapi.domain.hashtag.dto.FindAllHashtagResponse;
import com.kernelsquare.memberapi.domain.hashtag.dto.FindHashtagResponse;
import com.kernelsquare.memberapi.domain.hashtag.service.HashtagService;

@DisplayName("해시태그 컨트롤러 테스트")
@WithMockUser
@WebMvcTest(HashtagController.class)
class HashtagControllerTest extends RestDocsControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private HashtagService hashtagService;

	@Test
	@DisplayName("모든 해시태그 조회 성공시 200 OK와 응답 메시지를 반환한다.")
	void testFindAllHashtag() throws Exception {
		//given
		List<Hashtag> hashtags = List.of(
			Hashtag.builder().id(1L).content("김밥천국").build(),
			Hashtag.builder().id(2L).content("김밥천국").build());

		FindAllHashtagResponse response = FindAllHashtagResponse.from(
			List.of(
				FindHashtagResponse.from(hashtags.get(0)),
				FindHashtagResponse.from(hashtags.get(1))
			)
		);

		when(hashtagService.findAllHashtag()).thenReturn(response);

		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/hashtags")
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"));

		//then
		resultActions
			.andExpect(status().is(HASHTAG_ALL_FOUND.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("hashtag-all-found", getDocumentResponse(),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
					fieldWithPath("data.hashtags").type(JsonFieldType.ARRAY).description("해시태그 목록"),
					fieldWithPath("data.hashtags[].hashtag_id").type(JsonFieldType.NUMBER).description("해시태그 ID"),
					fieldWithPath("data.hashtags[].content").type(JsonFieldType.STRING).description("해시태그 내용")
				)
			));

		//verify
		verify(hashtagService, times(1)).findAllHashtag();
		verifyNoMoreInteractions(hashtagService);
	}

	@Test
	@DisplayName("해시태그 삭제 성공시 200 OK 와 응답 메시지를 반환한다.")
	void testDeleteHashtag() throws Exception {
		//given
		Long hashtagId = 1L;

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.delete("/api/v1/hashtags/{hashtagId}", hashtagId)
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"));

		//then
		resultActions
			.andExpect(status().is(HASHTAG_DELETED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("hashtag-deleted", getDocumentResponse(),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지")
				)
			));

		//verify
		verify(hashtagService, times(1)).deleteHashtag(hashtagId);
		verifyNoMoreInteractions(hashtagService);
	}
}