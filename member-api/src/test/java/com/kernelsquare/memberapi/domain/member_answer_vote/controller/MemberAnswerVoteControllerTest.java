package com.kernelsquare.memberapi.domain.member_answer_vote.controller;

import static com.kernelsquare.core.common_response.response.code.MemberAnswerVoteResponseCode.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.kernelsquare.memberapi.domain.member_answer_vote.dto.CreateMemberAnswerVoteRequest;
import com.kernelsquare.memberapi.domain.member_answer_vote.service.MemberAnswerVoteService;

@DisplayName("투표 컨트롤러 단위 테스트")
@WebMvcTest(MemberAnswerVoteController.class)
public class MemberAnswerVoteControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private MemberAnswerVoteService memberAnswerVoteService;
	private final Long testVoteId = 1L;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final CreateMemberAnswerVoteRequest createMemberAnswerVoteRequest = new CreateMemberAnswerVoteRequest(
		1L,
		1
	);

	@Test
	@WithMockUser
	@DisplayName("투표 생성 성공시, 200 OK, 메시지를 반환한다.")
	void testCreateVote() throws Exception {
		//given
		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		String jsonRequest = objectMapper.writeValueAsString(createMemberAnswerVoteRequest);

		//when & then
		mockMvc.perform(post("/api/v1/questions/answers/" + testVoteId + "/vote")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest))
			.andExpect(status().is(MEMBER_ANSWER_VOTE_CREATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(MEMBER_ANSWER_VOTE_CREATED.getCode()))
			.andExpect(jsonPath("$.msg").value(MEMBER_ANSWER_VOTE_CREATED.getMsg()));

		//verify
		verify(memberAnswerVoteService, times(1)).createVote(createMemberAnswerVoteRequest, testVoteId);
	}

	@Test
	@WithMockUser
	@DisplayName("투표 삭제 성공시, 200 OK, 메시지를 반환한다.")
	void testDeleteVote() throws Exception {
		//when & then
		mockMvc.perform(delete("/api/v1/questions/answers/" + testVoteId + "/vote")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"))
			.andExpect(status().is(MEMBER_ANSWER_VOTE_DELETED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(MEMBER_ANSWER_VOTE_DELETED.getCode()))
			.andExpect(jsonPath("$.msg").value(MEMBER_ANSWER_VOTE_DELETED.getMsg()));

		//verify
		verify(memberAnswerVoteService, times(1)).deleteVote(testVoteId);
	}
}
