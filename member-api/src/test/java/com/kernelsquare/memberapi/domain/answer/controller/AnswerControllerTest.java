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
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kernelsquare.domainmysql.domain.answer.entity.Answer;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member_answer_vote.entity.MemberAnswerVote;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.memberapi.domain.answer.dto.AnswerDto;
import com.kernelsquare.memberapi.domain.answer.dto.FindAllAnswerResponse;
import com.kernelsquare.memberapi.domain.answer.dto.FindAnswerResponse;
import com.kernelsquare.memberapi.domain.answer.dto.UpdateAnswerRequest;
import com.kernelsquare.memberapi.domain.answer.facade.AnswerFacade;
import com.kernelsquare.memberapi.domain.answer.service.AnswerService;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdaptorInstance;
import com.kernelsquare.memberapi.domain.chatgpt.service.ChatGptService;

@DisplayName("답변 컨트롤러 단위 테스트")
@WebMvcTest(AnswerController.class)
@Import(AnswerController.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
public class AnswerControllerTest {
	private final Long testQuestionId = 1L;
	private final Question testQuestion = Question
		.builder()
		.id(testQuestionId)
		.title("Test Question")
		.content("Test Content")
		.imageUrl("S3:TestImage")
		.closedStatus(false)
		.build();
	private final Member testMember = Member
		.builder()
		.id(1L)
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
		.id(1L)
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
		testAnswer.getMember().getId(),
		testQuestion.getId(),
		testAnswer.getContent(),
		1L,
		"s3:RankURL",
		testMember.getImageUrl(),
		testMember.getNickname(),
		testMember.getLevel().getName(),
		testAnswer.getImageUrl(),
		LocalDateTime.now(),
		LocalDateTime.now(),
		testAnswer.getVoteCount(),
		Long.valueOf(testMemberAnswerVote.getStatus())
	);

	private final UpdateAnswerRequest updateAnswerRequest = new UpdateAnswerRequest(
		"Test Updated Content",
		"Test Updated Image Url"
	);
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private AnswerService answerService;
	@MockBean
	private ChatGptService chatGptService;
	private List<FindAnswerResponse> answerResponseList = new ArrayList<>();
	private FindAllAnswerResponse answerResponseListDto;
	@MockBean
	private AnswerFacade answerFacade;
	private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
		.setPropertyNamingStrategy(PropertyNamingStrategies.SnakeCaseStrategy.INSTANCE);

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
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("커스텀 응답 코드"),
					fieldWithPath("data.answer_responses").type(JsonFieldType.ARRAY).description("답변 리스트"),
					fieldWithPath("data.answer_responses[].answer_id").type(JsonFieldType.NUMBER).description("답변 아이디"),
					fieldWithPath("data.answer_responses[].answer_member_id").type(JsonFieldType.NUMBER)
						.description("답변 작성자 아이디"),
					fieldWithPath("data.answer_responses[].member_nickname").type(JsonFieldType.STRING)
						.description("응답 메시지"),
					fieldWithPath("data.answer_responses[].question_id").type(JsonFieldType.NUMBER)
						.description("질문 아이디"),
					fieldWithPath("data.answer_responses[].content").type(JsonFieldType.STRING).description("내용"),
					fieldWithPath("data.answer_responses[].rank_image_url").type(JsonFieldType.STRING)
						.description("랭크 이미지 주소"),
					fieldWithPath("data.answer_responses[].rank_name").type(JsonFieldType.NUMBER)
						.description("랭크 명"),
					fieldWithPath("data.answer_responses[].member_image_url").type(JsonFieldType.STRING).description(
						"회원 프로필 사진 주소"),
					fieldWithPath("data.answer_responses[].author_level").type(JsonFieldType.NUMBER)
						.description("답변 작성자 레벨"),
					fieldWithPath("data.answer_responses[].answer_image_url").type(JsonFieldType.STRING).description(
						"답변 이미지 주소"),
					fieldWithPath("data.answer_responses[].created_date").type(JsonFieldType.STRING).description(
						"답변 생성일"),
					fieldWithPath("data.answer_responses[].modified_date").type(JsonFieldType.STRING).description(
						"답변 수정일"),
					fieldWithPath("data.answer_responses[].vote_count").type(JsonFieldType.NUMBER)
						.description("답변 투표수"),
					fieldWithPath("data.answer_responses[].vote_status").type(JsonFieldType.NUMBER).description(
						"투표 상태"))));

		//verify
		verify(answerService, times(1)).findAllAnswer(testQuestionId);
	}

	@Test
	@WithMockUser
	@DisplayName("답변 생성 성공시, 200 OK, 메시지를 반환한다.")
	void testCreateAnswer() throws Exception {
		//given
		AnswerDto.CreateRequest request = AnswerDto.CreateRequest.builder()
			.content("테스트 답변입니다~~~~~")
			.imageUrl("s3/answer/absoluteImageUrl.png")
			.build();

		MemberAdapter memberAdapter = new MemberAdapter(MemberAdaptorInstance.of(testMember));

		String jsonRequest = objectMapper.writeValueAsString(request);

		doNothing().when(answerFacade).createAnswer(any(AnswerDto.CreateRequest.class), anyLong(), eq(memberAdapter));

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/api/v1/questions/1/answers")
				.with(csrf())
				.with(user(memberAdapter))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest));

		//then
		resultActions.andExpect(status().is(ANSWER_CREATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("answer-created", getDocumentRequest(), getDocumentResponse(),
				requestFields(fieldWithPath("content").type(JsonFieldType.STRING).description("답변 내용"),
					fieldWithPath("image_url").type(JsonFieldType.STRING).description("답변 이미지")),
				responseFields(fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"))));

		//verify
		verify(answerFacade, times(1)).createAnswer(any(AnswerDto.CreateRequest.class), anyLong(), eq(memberAdapter));
	}

	@Test
	@WithMockUser
	@DisplayName("답변 수정 성공시, 200 OK, 메시지, 답변정보를 반환한다.")
	void testUpdateAnswer() throws Exception {
		//given
		MemberAdapter memberAdapter = new MemberAdapter(MemberAdaptorInstance.of(testMember));

		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		String jsonRequest = objectMapper.writeValueAsString(updateAnswerRequest);

		doNothing()
			.when(answerService)
			.updateAnswer(any(UpdateAnswerRequest.class), anyLong(), any(MemberAdapter.class));

		//when & then
		mockMvc.perform(put("/api/v1/questions/answers/" + testQuestionId)
				.with(csrf())
				.with(user(memberAdapter))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest))
			.andExpect(status().is(ANSWER_UPDATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(ANSWER_UPDATED.getCode()))
			.andExpect(jsonPath("$.msg").value(ANSWER_UPDATED.getMsg()));

		//verify
		verify(answerService, times(1)).updateAnswer(any(UpdateAnswerRequest.class), anyLong(), any(MemberAdapter.class));
	}

	@Test
	@WithMockUser
	@DisplayName("답변 삭제 성공시, 200 OK, 메시지, 답변정보를 반환한다.")
	void testDeleteAnswer() throws Exception {
		//given
		MemberAdapter memberAdapter = new MemberAdapter(MemberAdaptorInstance.of(testMember));

		doNothing()
			.when(answerService)
			.deleteAnswer(anyLong(), any(MemberAdapter.class));

		//when & then
		mockMvc.perform(delete("/api/v1/questions/answers/" + testQuestionId)
				.with(csrf())
				.with(user(memberAdapter))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"))
			.andExpect(status().is(ANSWER_DELETED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(ANSWER_DELETED.getCode()))
			.andExpect(jsonPath("$.msg").value(ANSWER_DELETED.getMsg()));

		//verify
		verify(answerService, times(1)).deleteAnswer(anyLong(), any(MemberAdapter.class));
	}
}
