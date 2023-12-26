package com.kernel360.kernelsquare.domain.question.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.kernel360.kernelsquare.domain.level.entity.Level;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.question.dto.CreateQuestionRequest;
import com.kernel360.kernelsquare.domain.question.dto.FindQuestionResponse;
import com.kernel360.kernelsquare.domain.question.dto.UpdateQuestionRequest;
import com.kernel360.kernelsquare.domain.question.entity.Question;
import com.kernel360.kernelsquare.domain.question.service.QuestionService;
import com.kernel360.kernelsquare.domain.tech_stack.entity.TechStack;
import com.kernel360.kernelsquare.domain.tech_stack.repository.TechStackRepository;
import com.kernel360.kernelsquare.global.dto.PageResponse;
import com.kernel360.kernelsquare.global.dto.Pagination;
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

import java.util.List;

import static com.kernel360.kernelsquare.global.common_response.response.code.QuestionResponseCode.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("질문 컨트롤러 통합 테스트")
@WithMockUser
@WebMvcTest(QuestionController.class)
class QuestionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private QuestionService questionService;
    @MockBean
    private TechStackRepository techStackRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    Member testMember;

    Question testQuestion;

    Level testLevel;

    private Question createTestQuestion() {
        return Question.builder()
            .title("테스트")
            .content("성공하자")
            .imageUrl("test.jpg")
            .viewCount(0L)
            .closedStatus(false)
            .member(testMember)
            .techStackList(List.of())
            .build();
    }

    private Member createTestMember() {
        return Member
            .builder()
            .nickname("hongjugwang")
            .email("jugwang@naver.com")
            .password("hashedPassword")
            .experience(10000L)
            .introduction("hi, i'm hongjugwang.")
            .imageUrl("s3:qwe12fasdawczx")
            .level(testLevel)
            .build();
    }

    private TechStack createTestTechStack(String skill) {
        return TechStack.builder()
            .skill(skill)
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
        testLevel = createTestLevel();

        TechStack techStack = createTestTechStack("Java");
        techStackRepository.save(techStack);

        techStack = createTestTechStack("Python");
        techStackRepository.save(techStack);

        testMember = createTestMember();

        testQuestion = createTestQuestion();
    }

    @Test
    @DisplayName("질문 생성 성공시 200 OK와 메시지를 반환한다")
    void testCreateQuestion() throws Exception {
        //given
        Long testMemberId = 1L;
        String testTitle = "createController";
        String testContent = "test";
        String testImageUrl = "con.jpg";
        List<String> testSkills = List.of();

        CreateQuestionRequest request = new CreateQuestionRequest(testMemberId, testTitle, testContent, testImageUrl, testSkills);

        Question testQuestion = CreateQuestionRequest.toEntity(request, testMember);

        doReturn(testQuestion.getId())
            .when(questionService)
            .createQuestion(any(CreateQuestionRequest.class));

        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        String jsonRequest = objectMapper.writeValueAsString(request);

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
            .andExpect(jsonPath("$.msg").value(QUESTION_CREATED.getMsg()));

        //verify
        verify(questionService, times(1)).createQuestion(any(CreateQuestionRequest.class));
    }

    @Test
    @DisplayName("질문 조회 성공시 200 OK와 메시지를 반환한다")
    void testFindQuestion() throws Exception {
        //given
        Long testQuestionId = 1L;

        FindQuestionResponse response = FindQuestionResponse.of(testMember, testQuestion, testLevel);

        doReturn(response)
            .when(questionService)
            .findQuestion(anyLong());

        //when & then
        mockMvc.perform(get("/api/v1/questions/" + testQuestionId)
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
        Pageable testPageable = PageRequest.of(0, 5);

        Pagination testPagination = Pagination.builder()
            .totalPage(1)
            .pageable(testPageable.getPageSize())
            .isEnd(true)
            .build();

        FindQuestionResponse question = FindQuestionResponse.of(testMember, testQuestion, testLevel);

        List<FindQuestionResponse> testResponsePages = List.of(question);

        PageResponse<FindQuestionResponse> response = PageResponse.of(testPagination, testResponsePages);

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
            .andExpect(jsonPath("$.msg").value(QUESTION_ALL_FOUND.getMsg()));

        //verify
        verify(questionService, times(1)).findAllQuestions(any(Pageable.class));
    }

    @Test
    @DisplayName("질문 수정 성공시 200 OK와 메시지를 반환한다")
    void testUpdateQuestion() throws Exception {
        //given
        Long testQuestionId = 1L;
        String testTitle = "put test";
        String testContent = "success";
        String testImageUrl = "put.jpg";
        List<String> testSkills = List.of();

        UpdateQuestionRequest request = new UpdateQuestionRequest(testTitle, testContent, testImageUrl, testSkills);

        doNothing()
            .when(questionService)
            .updateQuestion(anyLong(), any(UpdateQuestionRequest.class));

        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        String jsonRequest = objectMapper.writeValueAsString(request);

        //when & then
        mockMvc.perform(put("/api/v1/questions/" + testQuestionId)
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
        Long testQuestionId = 1L;

        doNothing()
            .when(questionService)
            .deleteQuestion(anyLong());

        //when & then
        mockMvc.perform(delete("/api/v1/questions/" + testQuestionId)
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