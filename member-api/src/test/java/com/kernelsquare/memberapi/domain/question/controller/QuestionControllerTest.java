package com.kernelsquare.memberapi.domain.question.controller;

import static com.kernelsquare.core.common_response.response.code.QuestionResponseCode.*;
import static com.kernelsquare.memberapi.config.ApiDocumentUtils.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import com.kernelsquare.memberapi.domain.question.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.core.dto.Pagination;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.memberapi.config.RestDocsControllerTest;
import com.kernelsquare.memberapi.domain.question.service.QuestionService;

@DisplayName("질문 컨트롤러 테스트")
@WithMockUser
@WebMvcTest(QuestionController.class)
class QuestionControllerTest extends RestDocsControllerTest {
	Member member;
	Level level;
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private QuestionService questionService;
	private ObjectMapper objectMapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

	private Question createTestQuestion(Long id) {
		return Question.builder()
			.id(id)
			.title("테스트하자자")
			.content("성공하자성공하자성공하자")
			.imageUrl("test.jpg")
			.viewCount(0L)
			.closedStatus(false)
			.member(member)
			.techStackList(List.of())
			.build();
	}

	private Member createTestMember() {
		return Member.builder()
			.id(1L)
			.nickname("hongjugwang")
			.email("jugwang@naver.com")
			.password("hashedPassword")
			.experience(10000L)
			.introduction("hi, i'm hongjugwang.")
			.imageUrl("s3:qwe12fasdawczx")
			.level(level)
			.build();
	}

	private Level createTestLevel() {
		return Level.builder()
			.name(6L)
			.imageUrl("1.jpg")
			.build();
	}

	@BeforeEach
	void setUp() {
		level = createTestLevel();

		member = createTestMember();
	}

	@Test
	@DisplayName("질문 생성 성공시 200 OK와 메시지를 반환한다")
	void testCreateQuestion() throws Exception {
		//given
		Question question = createTestQuestion(1L);

		CreateQuestionRequest createQuestionRequest =
			new CreateQuestionRequest(member.getId(), question.getTitle(), question.getContent(),
				question.getImageUrl(),
				question.getTechStackList().stream().map(x -> x.getTechStack().getSkill()).toList());

		CreateQuestionResponse createQuestionResponse = CreateQuestionResponse.from(question);

		given(questionService.createQuestion(any(CreateQuestionRequest.class))).willReturn(createQuestionResponse);

		String jsonRequest = objectMapper.writeValueAsString(createQuestionRequest);

		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/questions")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8")
			.content(jsonRequest));

		//then
		resultActions
			.andExpect(status().is(QUESTION_CREATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("question-created", getDocumentRequest(), getDocumentResponse(),
				requestFields(
					fieldWithPath("member_id").type(JsonFieldType.NUMBER).description("질문을 생성하는 회원의 ID"),
					fieldWithPath("title").type(JsonFieldType.STRING).description("질문 제목"),
					fieldWithPath("content").type(JsonFieldType.STRING).description("질문 내용"),
					fieldWithPath("image_url").type(JsonFieldType.STRING).description("질문 이미지 URL"),
					fieldWithPath("skills").type(JsonFieldType.ARRAY).description("질문에 사용된 기술 스택 목록")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("data.question_id").type(JsonFieldType.NUMBER).description("생성된 질문의 ID"))));

		//verify
		verify(questionService, times(1)).createQuestion(any(CreateQuestionRequest.class));
		verifyNoMoreInteractions(questionService);
	}

	@Test
	@DisplayName("질문 조회 성공시 200 OK와 메시지를 반환한다")
	void testFindQuestion() throws Exception {
		//given
		Question question = createTestQuestion(1L);

		FindQuestionResponse findQuestionResponse = FindQuestionResponse.builder()
			.id(question.getId())
			.title(question.getTitle())
			.content(question.getContent())
			.questionImageUrl(question.getImageUrl())
			.viewCount(question.getViewCount())
			.closeStatus(question.getClosedStatus())
			.memberId(member.getId())
			.nickname(member.getNickname())
			.memberImageUrl(member.getImageUrl())
			.level(level.getName())
			.levelImageUrl(level.getImageUrl())
			.skills(question.getTechStackList().stream().map(x -> x.getTechStack().getSkill()).toList())
			.createdDate(LocalDateTime.now())
			.modifiedDate(LocalDateTime.now())
			.build();

		given(questionService.findQuestion(anyLong())).willReturn(findQuestionResponse);

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/api/v1/questions/{questionId}", question.getId())
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"));

		//then
		resultActions
			.andExpect(status().is(QUESTION_FOUND.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("question-found", getDocumentRequest(), getDocumentResponse(),
				pathParameters(
					parameterWithName("questionId").description("조회할 질문의 ID")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("data.member_id").type(JsonFieldType.NUMBER).description("질문을 생성한 회원의 ID"),
					fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("질문을 생성한 회원의 닉네임"),
					fieldWithPath("data.member_image_url").type(JsonFieldType.STRING)
						.description("질문을 생성한 회원의 이미지 URL"),
					fieldWithPath("data.level").type(JsonFieldType.NUMBER).description("질문을 생성한 회원의 레벨"),
					fieldWithPath("data.level_image_url").type(JsonFieldType.STRING)
						.description("질문을 생성한 회원의 레벨 이미지 URL"),
					fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("질문 ID"),
					fieldWithPath("data.title").type(JsonFieldType.STRING).description("질문 제목"),
					fieldWithPath("data.content").type(JsonFieldType.STRING).description("질문 내용"),
					fieldWithPath("data.question_image_url").type(JsonFieldType.STRING).description("질문 이미지 URL"),
					fieldWithPath("data.view_count").type(JsonFieldType.NUMBER).description("질문 조회수"),
					fieldWithPath("data.close_status").type(JsonFieldType.BOOLEAN).description("질문 닫힘 여부"),
					fieldWithPath("data.created_date").type(JsonFieldType.STRING).description("질문 생성일"),
					fieldWithPath("data.modified_date").type(JsonFieldType.STRING).description("질문 수정일"),
					fieldWithPath("data.skills").type(JsonFieldType.ARRAY).description("질문에 사용된 기술 스택 목록")
				)));

		//verify
		verify(questionService, times(1)).findQuestion(anyLong());
		verifyNoMoreInteractions(questionService);
	}

	@Test
	@DisplayName("모든 질문 조회 성공시 200 OK와 메시지를 반환한다")
	void testFindAllQuestions() throws Exception {
		//given
		Question question1 = createTestQuestion(1L);
		Question question2 = createTestQuestion(2L);

		Pageable pageable = PageRequest.of(0, 2);
		Pagination pagination = Pagination.builder()
			.totalPage(1)
			.pageable(pageable.getPageSize())
			.isEnd(true)
			.build();

		FindQuestionResponse findQuestionResponse1 = FindQuestionResponse.builder()
			.id(question1.getId())
			.title(question1.getTitle())
			.content(question1.getContent())
			.questionImageUrl(question1.getImageUrl())
			.viewCount(question1.getViewCount())
			.closeStatus(question1.getClosedStatus())
			.memberId(member.getId())
			.nickname(member.getNickname())
			.memberImageUrl(member.getImageUrl())
			.level(level.getName())
			.levelImageUrl(level.getImageUrl())
			.skills(question1.getTechStackList().stream().map(x -> x.getTechStack().getSkill()).toList())
			.createdDate(LocalDateTime.now())
			.modifiedDate(LocalDateTime.now())
			.build();

		FindQuestionResponse findQuestionResponse2 = FindQuestionResponse.builder()
			.id(question2.getId())
			.title(question2.getTitle())
			.content(question2.getContent())
			.questionImageUrl(question2.getImageUrl())
			.viewCount(question2.getViewCount())
			.closeStatus(question2.getClosedStatus())
			.memberId(member.getId())
			.nickname(member.getNickname())
			.memberImageUrl(member.getImageUrl())
			.level(level.getName())
			.levelImageUrl(level.getImageUrl())
			.skills(question2.getTechStackList().stream().map(x -> x.getTechStack().getSkill()).toList())
			.createdDate(LocalDateTime.now())
			.modifiedDate(LocalDateTime.now())
			.build();

		List<FindQuestionResponse> responsePages = List.of(findQuestionResponse1, findQuestionResponse2);

		PageResponse<FindQuestionResponse> response = PageResponse.of(pagination, responsePages);

		doReturn(response)
			.when(questionService)
			.findAllQuestions(any(Pageable.class));

		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/questions")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"));

		//then
		resultActions
			.andExpect(status().is(QUESTION_ALL_FOUND.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("question-all-found", getDocumentRequest(), getDocumentResponse(),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("data.pagination.total_page").type(JsonFieldType.NUMBER).description("총 페이지 수"),
					fieldWithPath("data.pagination.pageable").type(JsonFieldType.NUMBER).description("페이지당 아이템 수"),
					fieldWithPath("data.pagination.is_end").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
					fieldWithPath("data.list[].member_id").type(JsonFieldType.NUMBER).description("질문을 생성한 회원의 ID"),
					fieldWithPath("data.list[].nickname").type(JsonFieldType.STRING).description("질문을 생성한 회원의 닉네임"),
					fieldWithPath("data.list[].member_image_url").type(JsonFieldType.STRING)
						.description("질문을 생성한 회원의 이미지 URL"),
					fieldWithPath("data.list[].level").type(JsonFieldType.NUMBER).description("질문을 생성한 회원의 레벨"),
					fieldWithPath("data.list[].level_image_url").type(JsonFieldType.STRING)
						.description("질문을 생성한 회원의 레벨 이미지 URL"),
					fieldWithPath("data.list[].id").type(JsonFieldType.NUMBER).description("질문 ID"),
					fieldWithPath("data.list[].title").type(JsonFieldType.STRING).description("질문 제목"),
					fieldWithPath("data.list[].content").type(JsonFieldType.STRING).description("질문 내용"),
					fieldWithPath("data.list[].question_image_url").type(JsonFieldType.STRING)
						.description("질문 이미지 URL"),
					fieldWithPath("data.list[].view_count").type(JsonFieldType.NUMBER).description("질문 조회수"),
					fieldWithPath("data.list[].close_status").type(JsonFieldType.BOOLEAN).description("질문 닫힘 여부"),
					fieldWithPath("data.list[].created_date").type(JsonFieldType.STRING).description("질문 생성일"),
					fieldWithPath("data.list[].modified_date").type(JsonFieldType.STRING).description("질문 수정일"),
					fieldWithPath("data.list[].skills").type(JsonFieldType.ARRAY).description("질문에 사용된 기술 스택 목록"))));

		//verify
		verify(questionService, times(1)).findAllQuestions(any(Pageable.class));
		verifyNoMoreInteractions(questionService);
	}

	@Test
	@DisplayName("질문 수정 성공시 200 OK와 메시지를 반환한다")
	void testUpdateQuestion() throws Exception {
		//given
		Question question = createTestQuestion(1L);

		UpdateQuestionRequest updateQuestionRequest =
			new UpdateQuestionRequest(question.getTitle(), question.getContent(), question.getImageUrl(), List.of());

		doNothing()
			.when(questionService)
			.updateQuestion(anyLong(), any(UpdateQuestionRequest.class));

		String jsonRequest = objectMapper.writeValueAsString(updateQuestionRequest);

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.put("/api/v1/questions/{questionId}", question.getId())
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest));

		//then
		resultActions
			.andExpect(status().is(QUESTION_UPDATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("question-updated", getDocumentRequest(), getDocumentResponse(),
				pathParameters(
					parameterWithName("questionId").description("수정할 질문의 ID")
				),
				requestFields(
					fieldWithPath("title").type(JsonFieldType.STRING).description("질문 제목"),
					fieldWithPath("content").type(JsonFieldType.STRING).description("질문 내용"),
					fieldWithPath("image_url").type(JsonFieldType.STRING).description("질문 이미지 URL"),
					fieldWithPath("skills").type(JsonFieldType.ARRAY).description("질문에 사용된 기술 스택 목록")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"))));

		//verify
		verify(questionService, times(1)).updateQuestion(anyLong(), any(UpdateQuestionRequest.class));
		verifyNoMoreInteractions(questionService);
	}

	@Test
	@DisplayName("질문 삭제 성공시 200 OK와 메시지를 반환한다")
	void testDeleteQuestion() throws Exception {
		//given
		Question question = createTestQuestion(1L);

		doNothing()
			.when(questionService)
			.deleteQuestion(anyLong());

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.delete("/api/v1/questions/" + question.getId())
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"));

		//then
		resultActions
			.andExpect(status().is(QUESTION_DELETED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("question-deleted", getDocumentRequest(), getDocumentResponse(),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"))));

		//verify
		verify(questionService, times(1)).deleteQuestion(anyLong());
	}

	@Test
	@DisplayName("SEO 최적화를 위한 모든 질문 조회 성공시 200 OK와 메시지를 반환한다")
	void testFindAllQuestionsSeo() throws Exception {
		//given
		Question question01 = createTestQuestion(1L);
		Question question02 = createTestQuestion(2L);

		FindQuestionIdResponse findQuestionIdResponse01 = FindQuestionIdResponse.of(question01.getId());
		FindQuestionIdResponse findQuestionIdResponse02 = FindQuestionIdResponse.of(question02.getId());

		FindAllQuestionResponse response =
				FindAllQuestionResponse.of(List.of(findQuestionIdResponse01, findQuestionIdResponse02));

		//when & then
		doReturn(response)
				.when(questionService)
				.findAllQuestionsSeo();

		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/api/v1/questions/seo")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
		);

		resultActions
				.andExpect(status().is(QUESTION_SEO_LIST_FOUND.getStatus().value()))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andDo(
						document("question-seo-list-found", getDocumentResponse(),
								responseFields(
										fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
										fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
										fieldWithPath("data.question_id_list[0].question_id").type(JsonFieldType.NUMBER).description("질문 아이디 01"),
										fieldWithPath("data.question_id_list[1].question_id").type(JsonFieldType.NUMBER).description("질문 아이디 02")
								)
						));

		//verify
		verify(questionService, times(1)).findAllQuestionsSeo();
	}
}