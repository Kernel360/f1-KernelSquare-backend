package com.kernelsquare.adminapi.domain.hashtag.controller;

import static com.kernelsquare.core.common_response.response.code.HashtagResponseCode.HashTagResponseCode.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.kernelsquare.adminapi.domain.hashtag.dto.FindAllHashtagResponse;
import com.kernelsquare.adminapi.domain.hashtag.dto.FindHashtagResponse;
import com.kernelsquare.adminapi.domain.hashtag.service.HashtagService;
import com.kernelsquare.domainmysql.domain.hashtag.entity.Hashtag;

@DisplayName("해시태그 컨트롤러 단위 테스트")
@WithMockUser
@WebMvcTest(HashtagController.class)
class HashtagControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private HashtagService hashtagService;

	@Test
	@DisplayName("모든 해시태그 조회 성공시 200 OK와 응답 메시지를 반환한다.")
	void testFindAllHashtag() throws Exception {
		// Given
		FindHashtagResponse response1 = new FindHashtagResponse(1L, "#testtag");
		FindHashtagResponse response2 = new FindHashtagResponse(2L, "#mentoring");

		FindAllHashtagResponse allHashtagResponse = FindAllHashtagResponse.from(List.of(response1, response2));

		doReturn(allHashtagResponse)
			.when(hashtagService)
			.findAllHashtag();

		// When & Then
		mockMvc.perform(get("/api/v1/hashtags")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"))
			.andExpect(status().is(HASHTAG_ALL_FOUND.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(HASHTAG_ALL_FOUND.getCode()))
			.andExpect(jsonPath("$.msg").value(HASHTAG_ALL_FOUND.getMsg()));

		// Then
		verify(hashtagService, times(1)).findAllHashtag();
	}

	@Test
	@WithMockUser
	@DisplayName("해시태그 삭제 성공시 200 OK 와 응답 메시지를 반환한다.")
	void testDeleteHashtag() throws Exception {
		// Given
		Hashtag hashtag = Hashtag.builder().id(1L).content("#김밥천국").build();

		doNothing()
			.when(hashtagService)
			.deleteHashtag(anyLong());

		// When & Then
		mockMvc.perform(delete("/api/v1/hashtags/" + hashtag.getId())
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"))
			.andExpect(status().is(HASHTAG_DELETED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(HASHTAG_DELETED.getCode()))
			.andExpect(jsonPath("$.msg").value(HASHTAG_DELETED.getMsg()));

		// Verify
		verify(hashtagService, times(1)).deleteHashtag(anyLong());
	}
}