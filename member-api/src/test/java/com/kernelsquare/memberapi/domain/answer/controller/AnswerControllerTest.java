package com.kernelsquare.memberapi.domain.answer.controller;

import static com.kernelsquare.core.common_response.response.code.AnswerResponseCode.*;
import static com.kernelsquare.memberapi.config.ApiDocumentUtils.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.kernelsquare.domainmysql.domain.answer.entity.Answer;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member_answer_vote.entity.MemberAnswerVote;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.memberapi.domain.answer.dto.CreateAnswerRequest;
import com.kernelsquare.memberapi.domain.answer.dto.FindAllAnswerResponse;
import com.kernelsquare.memberapi.domain.answer.dto.FindAnswerResponse;
import com.kernelsquare.memberapi.domain.answer.dto.UpdateAnswerRequest;
import com.kernelsquare.memberapi.domain.answer.service.AnswerService;

@DisplayName("답변 컨트롤러 단위 테스트")
@WebMvcTest(AnswerController.class)
@Import(AnswerController.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
public class AnswerControllerTest {
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
	private final CreateAnswerRequest createAnswerRequest = new CreateAnswerRequest(
		1L,
		"Test Content",
		"Test Image Url"
	);
	private final UpdateAnswerRequest updateAnswerRequest = new UpdateAnswerRequest(
		"Test Updated Content",
		"Test Updated Image Url"
	);
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private AnswerService answerService;
	private List<FindAnswerResponse> answerResponseList = new ArrayList<>();
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

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/api/v1/questions/{questionId}/answers", testQuestionId)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"));

		//then
		resultActions
			.andExpect(status().is(ANSWERS_ALL_FOUND.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("answer-all-found",
				getDocumentResponse(),
				pathParameters(
					parameterWithName("questionId").description("아이디")
				),
				responseFields(
					fieldWithPath("data").description("응답"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("code").description("The status code of the response."),
					fieldWithPath("data.answer_responses").description("Array containing answer responses."),
					fieldWithPath("data.answer_responses[].answer_id").description("The answer ID."),
					fieldWithPath("data.answer_responses[].question_id").description("The question ID."),
					fieldWithPath("data.answer_responses[].content").description("The content of the answer."),
					fieldWithPath("data.answer_responses[].rank_image_url").description("The URL of the rank image."),
					fieldWithPath("data.answer_responses[].member_image_url").description(
						"The URL of the member image."),
					fieldWithPath("data.answer_responses[].created_by").description("The username of the creator."),
					fieldWithPath("data.answer_responses[].author_level").description("The author level."),
					fieldWithPath("data.answer_responses[].answer_image_url").description(
						"The URL of the answer image."),
					fieldWithPath("data.answer_responses[].created_date").description(
						"The creation date of the answer."),
					fieldWithPath("data.answer_responses[].modified_date").description(
						"The modification date of the answer."),
					fieldWithPath("data.answer_responses[].vote_count").description(
						"The number of votes for the answer."),
					fieldWithPath("data.answer_responses[].vote_status").description(
						"The vote status of the answer."))));

		//verify
		verify(answerService, times(1)).findAllAnswer(testQuestionId);
	}

	@Test
	@WithMockUser
	@DisplayName("답변 생성 성공시, 200 OK, 메시지를 반환한다.")
	void testCreateAnswer() throws Exception {
		//given
		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		String jsonRequest = objectMapper.writeValueAsString(createAnswerRequest);

		//when & then
		mockMvc.perform(post("/api/v1/questions/" + testQuestionId + "/answers")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest))
			.andExpect(status().is(ANSWER_CREATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(ANSWER_CREATED.getCode()))
			.andExpect(jsonPath("$.msg").value(ANSWER_CREATED.getMsg()));

		//verify
		verify(answerService, times(1)).createAnswer(createAnswerRequest, testQuestionId);
	}

	@Test
	@WithMockUser
	@DisplayName("답변 수정 성공시, 200 OK, 메시지, 답변정보를 반환한다.")
	void testUpdateAnswer() throws Exception {
		//given
		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		String jsonRequest = objectMapper.writeValueAsString(updateAnswerRequest);

		doNothing()
			.when(answerService)
			.updateAnswer(any(UpdateAnswerRequest.class), anyLong());

		//when & then
		mockMvc.perform(put("/api/v1/questions/answers/" + testQuestionId)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest))
			.andExpect(status().is(ANSWER_UPDATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(ANSWER_UPDATED.getCode()))
			.andExpect(jsonPath("$.msg").value(ANSWER_UPDATED.getMsg()));

		//verify
		verify(answerService, times(1)).updateAnswer(updateAnswerRequest, testQuestionId);
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
