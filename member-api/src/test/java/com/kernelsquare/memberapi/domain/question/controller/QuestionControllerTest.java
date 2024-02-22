package com.kernelsquare.memberapi.domain.question.controller;

import static com.kernelsquare.core.common_response.response.code.QuestionResponseCode.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.kernelsquare.memberapi.domain.question.dto.CreateQuestionRequest;
import com.kernelsquare.memberapi.domain.question.dto.CreateQuestionResponse;
import com.kernelsquare.memberapi.domain.question.dto.FindQuestionResponse;
import com.kernelsquare.memberapi.domain.question.dto.UpdateQuestionRequest;
import com.kernelsquare.memberapi.domain.question.service.QuestionService;
import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.core.dto.Pagination;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.question.entity.Question;

@DisplayName("질문 컨트롤러 단위 테스트")
@WithMockUser
@WebMvcTest(QuestionController.class)
class QuestionControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private QuestionService questionService;

	private ObjectMapper objectMapper = new ObjectMapper();

	Member member;
	Level level;

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

		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		String jsonRequest = objectMapper.writeValueAsString(createQuestionRequest);

		//when & then
		mockMvc.perform(post("/api/v1/questions")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest))
			.andExpect(status().is(QUESTION_CREATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(QUESTION_CREATED.getCode()))
			.andExpect(jsonPath("$.msg").value(QUESTION_CREATED.getMsg()))
			.andExpect(jsonPath("$.data.question_id").value(question.getId()));

		//verify
		verify(questionService, times(1)).createQuestion(any(CreateQuestionRequest.class));
	}

	@Test
	@DisplayName("질문 조회 성공시 200 OK와 메시지를 반환한다")
	void testFindQuestion() throws Exception {
		//given
		Question question = createTestQuestion(1L);

		FindQuestionResponse findQuestionResponse = FindQuestionResponse.of(member, question, level);

		given(questionService.findQuestion(anyLong())).willReturn(findQuestionResponse);

		//when & then
		mockMvc.perform(get("/api/v1/questions/" + question.getId())
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"))
			.andExpect(status().is(QUESTION_FOUND.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(QUESTION_FOUND.getCode()))
			.andExpect(jsonPath("$.msg").value(QUESTION_FOUND.getMsg()));

		//verify
		verify(questionService, times(1)).findQuestion(anyLong());
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

		FindQuestionResponse findQuestionResponse1 = FindQuestionResponse.of(member, question1, level);
		FindQuestionResponse findQuestionResponse2 = FindQuestionResponse.of(member, question2, level);

		List<FindQuestionResponse> responsePages = List.of(findQuestionResponse1, findQuestionResponse2);

		PageResponse<FindQuestionResponse> response = PageResponse.of(pagination, responsePages);

		doReturn(response)
			.when(questionService)
			.findAllQuestions(any(Pageable.class));

		//when & then
		mockMvc.perform(get("/api/v1/questions")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"))
			.andExpect(status().is(QUESTION_ALL_FOUND.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(QUESTION_ALL_FOUND.getCode()))
			.andExpect(jsonPath("$.msg").value(QUESTION_ALL_FOUND.getMsg()))
			.andExpect(jsonPath("$.data.pagination.total_page").value(1))
			.andExpect(jsonPath("$.data.pagination.pageable").value(pageable.getPageSize()))
			.andExpect(jsonPath("$.data.pagination.is_end").value(true));

		//verify
		verify(questionService, times(1)).findAllQuestions(any(Pageable.class));
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

		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		String jsonRequest = objectMapper.writeValueAsString(updateQuestionRequest);

		//when & then
		mockMvc.perform(put("/api/v1/questions/" + question.getId())
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest))
			.andExpect(status().is(QUESTION_UPDATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(QUESTION_UPDATED.getCode()))
			.andExpect(jsonPath("$.msg").value(QUESTION_UPDATED.getMsg()));

		//verify
		verify(questionService, times(1)).updateQuestion(anyLong(), any(UpdateQuestionRequest.class));
	}

	@Test
	@DisplayName("질문 삭제 성공시 200 OK와 메시지를 반환한다")
	void testDeleteQuestion() throws Exception {
		//given
		Question question = createTestQuestion(1L);

		doNothing()
			.when(questionService)
			.deleteQuestion(anyLong());

		//when & then
		mockMvc.perform(delete("/api/v1/questions/" + question.getId())
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"))
			.andExpect(status().is(QUESTION_DELETED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(QUESTION_DELETED.getCode()))
			.andExpect(jsonPath("$.msg").value(QUESTION_DELETED.getMsg()));

		//verify
		verify(questionService, times(1)).deleteQuestion(anyLong());
	}
}