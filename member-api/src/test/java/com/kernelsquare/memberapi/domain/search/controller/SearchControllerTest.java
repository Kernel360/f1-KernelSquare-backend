package com.kernelsquare.memberapi.domain.search.controller;

import static com.kernelsquare.core.common_response.response.code.SearchResponseCode.*;
import static com.kernelsquare.memberapi.config.ApiDocumentUtils.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.core.dto.Pagination;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;
import com.kernelsquare.memberapi.config.RestDocsControllerTest;
import com.kernelsquare.memberapi.domain.question.dto.FindQuestionResponse;
import com.kernelsquare.memberapi.domain.search.dto.SearchQuestionResponse;
import com.kernelsquare.memberapi.domain.search.dto.SearchTechStackResponse;
import com.kernelsquare.memberapi.domain.search.service.SearchService;

@DisplayName("검색 컨트롤러 단위 테스트")
@WebMvcTest(SearchController.class)
class SearchControllerTest extends RestDocsControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private SearchService searchService;

	private Member member;
	private Level level;

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

	private Question createTestQuestion(Long id) {
		return Question.builder()
			.id(id)
			.title("테스트")
			.content("성공하자")
			.imageUrl("test.jpg")
			.viewCount(0L)
			.closedStatus(false)
			.member(member)
			.techStackList(List.of())
			.build();
	}

	@BeforeEach
	void setUp() {
		level = createTestLevel();

		member = createTestMember();
	}

	@Test
	@WithMockUser
	@DisplayName("질문 검색 성공시 200 OK와 응답 메시지를 반환한다")
	void testSearchQuestions() throws Exception {
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

		PageResponse<FindQuestionResponse> pageQuestionList = PageResponse.of(pagination, responsePages);

		SearchQuestionResponse response = SearchQuestionResponse.of(2L, pageQuestionList);

		doReturn(response)
			.when(searchService)
			.searchQuestions(any(Pageable.class), anyString());

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/api/v1/search/questions?keyword=테스트")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"));

		//then
		resultActions
			.andExpect(status().is(SEARCH_QUESTION_COMPLETED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("search-question-completed", getDocumentRequest(), getDocumentResponse(),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("data.total_count").type(JsonFieldType.NUMBER).description("검색된 질문의 총 개수"),
					fieldWithPath("data.pagination.total_page").type(JsonFieldType.NUMBER)
						.description("검색된 질문의 총 페이지 수"),
					fieldWithPath("data.pagination.pageable").type(JsonFieldType.NUMBER)
						.description("한 페이지에 보여질 질문의 개수"),
					fieldWithPath("data.pagination.is_end").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
					fieldWithPath("data.question_list[].id").type(JsonFieldType.NUMBER).description("질문 ID"),
					fieldWithPath("data.question_list[].title").type(JsonFieldType.STRING).description("질문 제목"),
					fieldWithPath("data.question_list[].content").type(JsonFieldType.STRING).description("질문 내용"),
					fieldWithPath("data.question_list[].question_image_url").type(JsonFieldType.STRING)
						.description("질문 이미지 URL"),
					fieldWithPath("data.question_list[].view_count").type(JsonFieldType.NUMBER).description("질문 조회수"),
					fieldWithPath("data.question_list[].close_status").type(JsonFieldType.BOOLEAN)
						.description("질문 답변 완료 여부"),
					fieldWithPath("data.question_list[].member_id").type(JsonFieldType.NUMBER).description("질문 작성자 ID"),
					fieldWithPath("data.question_list[].nickname").type(JsonFieldType.STRING).description("질문 작성자 닉네임"),
					fieldWithPath("data.question_list[].member_image_url").type(JsonFieldType.STRING)
						.description("질문 작성자 이미지 URL"),
					fieldWithPath("data.question_list[].level").type(JsonFieldType.NUMBER).description("질문 작성자 레벨 ID"),
					fieldWithPath("data.question_list[].level_image_url").type(JsonFieldType.STRING)
						.description("질문 작성자 레벨 이미지 URL"),
					fieldWithPath("data.question_list[].skills").type(JsonFieldType.ARRAY).description("질문 기술 스택 목록"),
					fieldWithPath("data.question_list[].created_date").type(JsonFieldType.STRING).description("질문 작성일"),
					fieldWithPath("data.question_list[].modified_date").type(JsonFieldType.STRING).description("질문 수정일")
				)));

		//verify
		verify(searchService, times(1)).searchQuestions(any(Pageable.class), anyString());
		verifyNoMoreInteractions(searchService);
	}

	@Test
	@WithMockUser
	@DisplayName("기술 스택 검색 성공시 200 OK와 응답 메시지를 반환한다")
	void testSearchTechStacks() throws Exception {
		//given
		TechStack techStack1 = new TechStack(1L, "JavaScript");
		TechStack techStack2 = new TechStack(2L, "Python");

		Pageable pageable = PageRequest.of(0, 2);
		Pagination pagination = Pagination.builder()
			.totalPage(1)
			.pageable(pageable.getPageSize())
			.isEnd(true)
			.build();

		List<TechStack> testSkills = List.of(techStack1, techStack2);

		List<String> responsePage = testSkills.stream()
			.map(TechStack::getSkill)
			.toList();

		PageResponse<String> response = PageResponse.of(pagination, responsePage);

		SearchTechStackResponse searchResults = SearchTechStackResponse.of(2L, response);

		doReturn(searchResults)
			.when(searchService)
			.searchTechStacks(any(Pageable.class), anyString());

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/api/v1/search/techs?keyword=j")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"));

		//then
		resultActions
			.andExpect(status().is(SEARCH_TECH_STACK_COMPLETED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("search-tech-stack-completed", getDocumentRequest(), getDocumentResponse(),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("data.total_count").type(JsonFieldType.NUMBER).description("검색된 기술 스택의 총 개수"),
					fieldWithPath("data.pagination.total_page").type(JsonFieldType.NUMBER)
						.description("검색된 기술 스택의 총 페이지 수"),
					fieldWithPath("data.pagination.pageable").type(JsonFieldType.NUMBER)
						.description("한 페이지에 보여질 기술 스택의 개수"),
					fieldWithPath("data.pagination.is_end").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
					fieldWithPath("data.tech_stack_list[]").type(JsonFieldType.ARRAY).description("기술 스택 목록")
				)));

		//verify
		verify(searchService, times(1)).searchTechStacks(any(Pageable.class), anyString());
	}
}