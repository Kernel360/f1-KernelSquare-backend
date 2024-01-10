package com.kernel360.kernelsquare.domain.search.controller;

import com.kernel360.kernelsquare.domain.level.entity.Level;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.question.dto.FindQuestionResponse;
import com.kernel360.kernelsquare.domain.question.entity.Question;
import com.kernel360.kernelsquare.domain.search.service.SearchService;
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

import static com.kernel360.kernelsquare.global.common_response.response.code.SearchResponseCode.SEARCH_QUESTION_COMPLETED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("검색 컨트롤러 통합 테스트")
@WebMvcTest(SearchController.class)
class SearchControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SearchService searchService;

    Member member;
    Level level;

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
    @DisplayName("검색 성공시 200 OK와 응답 메시지를 반환한다")
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

        FindQuestionResponse findQuestionResponse1 = FindQuestionResponse.of(member, question1, level);
        FindQuestionResponse findQuestionResponse2 = FindQuestionResponse.of(member, question2, level);

        List<FindQuestionResponse> responsePages = List.of(findQuestionResponse1, findQuestionResponse2);

        PageResponse<FindQuestionResponse> response = PageResponse.of(pagination, responsePages);

        doReturn(response)
            .when(searchService)
            .searchQuestions(any(Pageable.class), anyString());

        //when & then
        mockMvc.perform(get("/api/v1/search/questions?keyword=테스트")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
            .andExpect(status().is(SEARCH_QUESTION_COMPLETED.getStatus().value()))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(SEARCH_QUESTION_COMPLETED.getCode()))
            .andExpect(jsonPath("$.msg").value(SEARCH_QUESTION_COMPLETED.getMsg()));

        //verify
        verify(searchService, times(1)).searchQuestions(any(Pageable.class), anyString());
    }
}