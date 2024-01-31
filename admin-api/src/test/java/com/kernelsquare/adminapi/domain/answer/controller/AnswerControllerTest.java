package com.kernelsquare.adminapi.domain.answer.controller;

import static com.kernelsquare.core.common_response.response.code.AnswerResponseCode.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kernelsquare.adminapi.domain.answer.dto.FindAllAnswerResponse;
import com.kernelsquare.adminapi.domain.answer.dto.FindAnswerResponse;
import com.kernelsquare.adminapi.domain.answer.service.AnswerService;
import com.kernelsquare.domainmysql.domain.answer.entity.Answer;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member_answer_vote.entity.MemberAnswerVote;
import com.kernelsquare.domainmysql.domain.question.entity.Question;

@DisplayName("답변 컨트롤러 단위 테스트")
@WebMvcTest(AnswerController.class)
public class AnswerControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private AnswerService answerService;
	private final Long testQuestionId = 1L;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final Question testQuestion = Question
		.builder()
		.title("Test Question")
		.content("Test Content")
		.imageUrl("S3:TestImage")
		.closedStatus(false)
		.build();

	private final Member testMember = Member
		.builder()
		.nickname("hongjugwang")
		.email("jugwang@naver.com")
		.password("hashedPassword")
		.experience(10000L)
		.introduction("hi, i'm hongjugwang.")
		.level(Level.builder()
			.name(1L)
			.imageUrl("s3:testLevelImage")
			.levelUpperLimit(500L)
			.build())
		.imageUrl("s3:qwe12fasdawczx")
		.build();

	private final Answer testAnswer = Answer
		.builder()
		.content("Test Answer Content")
		.voteCount(10L)
		.imageUrl("s3:AnswerImageURL")
		.member(testMember)
		.question(testQuestion)
		.build();

	private final MemberAnswerVote testMemberAnswerVote = MemberAnswerVote
		.builder()
		.status(-1)
		.member(testMember)
		.answer(testAnswer)
		.build();
	private final FindAnswerResponse findAnswerResponse = new FindAnswerResponse(
		testAnswer.getId(),
		testQuestion.getId(),
		testAnswer.getContent(),
		"s3:RankURL",
		testMember.getImageUrl(),
		testMember.getNickname(),
		testMember.getLevel().getName(),
		testAnswer.getImageUrl(),
		LocalDateTime.now(),
		null,
		testAnswer.getVoteCount(),
		Long.valueOf(testMemberAnswerVote.getStatus())
	);

	private final List<FindAnswerResponse> answerResponseList = new ArrayList<>();
	private FindAllAnswerResponse answerResponseListDto;

	@Test
	@WithMockUser
	@DisplayName("답변 조회 성공시, 200 OK, 메시지, 답변정보를 반환한다.")
	void testFindAllAnswers() throws Exception {
		//given
		answerResponseList.add(findAnswerResponse);
		answerResponseListDto = FindAllAnswerResponse.from(answerResponseList);

		doReturn(answerResponseListDto)
			.when(answerService)
			.findAllAnswer(anyLong());

		//when & then
		mockMvc.perform(get("/api/v1/questions/" + testQuestionId + "/answers")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"))
			.andExpect(status().is(ANSWERS_ALL_FOUND.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(ANSWERS_ALL_FOUND.getCode()))
			.andExpect(jsonPath("$.msg").value(ANSWERS_ALL_FOUND.getMsg()))
			.andExpect(jsonPath("$.data.answer_responses[0].content").value(testAnswer.getContent()))
			.andExpect(jsonPath("$.data.answer_responses[0].answer_image_url").value(testAnswer.getImageUrl()))
			.andExpect(jsonPath("$.data.answer_responses[0].vote_count").value(testAnswer.getVoteCount()))
			.andExpect(jsonPath("$.data.answer_responses[0].vote_status").value(testMemberAnswerVote.getStatus()));

		//verify
		verify(answerService, times(1)).findAllAnswer(testQuestionId);
	}

	@Test
	@WithMockUser
	@DisplayName("답변 삭제 성공시, 200 OK, 메시지, 답변정보를 반환한다.")
	void testDeleteAnswer() throws Exception {
		//given
		doNothing()
			.when(answerService)
			.deleteAnswer(anyLong());

		//when & then
		mockMvc.perform(delete("/api/v1/questions/answers/" + testQuestionId)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"))
			.andExpect(status().is(ANSWER_DELETED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(ANSWER_DELETED.getCode()))
			.andExpect(jsonPath("$.msg").value(ANSWER_DELETED.getMsg()));

		//verify
		verify(answerService, times(1)).deleteAnswer(testQuestionId);
	}
}
