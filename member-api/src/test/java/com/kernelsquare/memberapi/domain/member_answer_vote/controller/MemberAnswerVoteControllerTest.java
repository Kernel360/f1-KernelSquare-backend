package com.kernelsquare.memberapi.domain.member_answer_vote.controller;

import static com.kernelsquare.core.common_response.response.code.MemberAnswerVoteResponseCode.*;
import static com.kernelsquare.memberapi.config.ApiDocumentUtils.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.kernelsquare.memberapi.config.RestDocsControllerTest;
import com.kernelsquare.memberapi.domain.member_answer_vote.dto.CreateMemberAnswerVoteRequest;
import com.kernelsquare.memberapi.domain.member_answer_vote.service.MemberAnswerVoteService;

@DisplayName("투표 컨트롤러 테스트")
@WebMvcTest(MemberAnswerVoteController.class)
class MemberAnswerVoteControllerTest extends RestDocsControllerTest {
	private final Long testVoteId = 1L;
	private final ObjectMapper objectMapper = new ObjectMapper().setPropertyNamingStrategy(
		PropertyNamingStrategy.SNAKE_CASE);
	private final CreateMemberAnswerVoteRequest createMemberAnswerVoteRequest = new CreateMemberAnswerVoteRequest(
		1L,
		1
	);
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private MemberAnswerVoteService memberAnswerVoteService;

	@Test
	@WithMockUser
	@DisplayName("투표 생성 성공시, 200 OK, 메시지를 반환한다.")
	void testCreateVote() throws Exception {
		//given
		String jsonRequest = objectMapper.writeValueAsString(createMemberAnswerVoteRequest);

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/api/v1/questions/answers/" + testVoteId + "/vote")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest));

		//then
		resultActions
			.andExpect(status().is(MEMBER_ANSWER_VOTE_CREATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("member-answer-vote-created", getDocumentRequest(), getDocumentResponse(),
				requestFields(
					fieldWithPath("member_id").type(JsonFieldType.NUMBER).description("답변 ID"),
					fieldWithPath("status").type(JsonFieldType.NUMBER).description("투표 상태")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지")
				)
			));

		//verify
		verify(memberAnswerVoteService, times(1)).createVote(createMemberAnswerVoteRequest, testVoteId);
	}

	@Test
	@WithMockUser
	@DisplayName("투표 삭제 성공시, 200 OK, 메시지를 반환한다.")
	void testDeleteVote() throws Exception {
		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.delete("/api/v1/questions/answers/" + testVoteId + "/vote")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"));

		//then
		resultActions
			.andExpect(status().is(MEMBER_ANSWER_VOTE_DELETED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("member-answer-vote-deleted", getDocumentResponse(),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지")
				)
			));

		//verify
		verify(memberAnswerVoteService, times(1)).deleteVote(testVoteId);
	}
}
