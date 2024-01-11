package com.kernel360.kernelsquare.domain.search.service;

import com.kernel360.kernelsquare.domain.level.entity.Level;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.question.dto.FindQuestionResponse;
import com.kernel360.kernelsquare.domain.question.entity.Question;
import com.kernel360.kernelsquare.domain.search.repository.SearchRepository;
import com.kernel360.kernelsquare.global.dto.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("검색 서비스 통합 테스트")
@ExtendWith(MockitoExtension.class)
class SearchServiceTest {
    @InjectMocks
    private SearchService searchService;
    @Mock
    private SearchRepository searchRepository;

    private Member member;

    private Level level;

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

    private Member createTestMember() {
        return Member
            .builder()
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
    @DisplayName("질문 검색 테스트")
    void testFindAllQuestions() {
        //given
        Question question1 = createTestQuestion(1L);
        Question question2 = createTestQuestion(2L);
        List<Question> questions = List.of(question1, question2);

        Pageable pageable = PageRequest.of(0, 2);
        Page<Question> pages = new PageImpl<>(questions, pageable, questions.size());

        String keyword = "테스트";

        given(searchRepository.searchQuestionsByKeyword(pageable, keyword)).willReturn(pages);

        Integer currentPage = pageable.getPageNumber() + 1;

        Integer totalPages = pages.getTotalPages();

        if (totalPages == 0) totalPages+=1;

        //when
        PageResponse<FindQuestionResponse> pageResponse = searchService.searchQuestions(pageable, keyword);

        //then
        assertThat(pageResponse).isNotNull();
        assertThat(pageResponse.pagination().totalPage()).isEqualTo(totalPages);
        assertThat(pageResponse.pagination().pageable()).isEqualTo(pages.getSize());
        assertThat(pageResponse.pagination().isEnd()).isEqualTo(currentPage.equals(totalPages));
        assertThat(pageResponse.list()).isNotNull();

        //verify
        verify(searchRepository, times(1)).searchQuestionsByKeyword(any(Pageable.class), anyString());
    }

}