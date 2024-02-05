package com.kernelsquare.memberapi.domain.search.service;

import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.domainmysql.domain.search.repository.SearchRepository;
import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;
import com.kernelsquare.memberapi.domain.search.dto.SearchQuestionResponse;
import com.kernelsquare.memberapi.domain.search.dto.SearchTechStackResponse;
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
import static org.mockito.BDDMockito.*;

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
	void testSearchQuestions() {
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

		if (totalPages == 0)
			totalPages += 1;

		//when
		SearchQuestionResponse searchResults = searchService.searchQuestions(pageable, keyword);

		//then
		assertThat(searchResults).isNotNull();
		assertThat(searchResults.pagination().totalPage()).isEqualTo(totalPages);
		assertThat(searchResults.pagination().pageable()).isEqualTo(pages.getSize());
		assertThat(searchResults.pagination().isEnd()).isEqualTo(currentPage.equals(totalPages));
		assertThat(searchResults.questionList()).isNotNull();

		//verify
		verify(searchRepository, times(1)).searchQuestionsByKeyword(any(Pageable.class), anyString());
	}

	@Test
	@DisplayName("기술 스택 검색 테스트")
	void testSearchTechStacks() {
		//given
		TechStack techStack1 = new TechStack(1L, "JavaScript");
		TechStack techStack2 = new TechStack(2L, "Python");

		List<TechStack> testSkills = List.of(techStack1, techStack2);

		Pageable pageable = PageRequest.of(0, 2);
		Page<TechStack> pages = new PageImpl<>(testSkills, pageable, testSkills.size());

		String keyword = "j";

		given(searchRepository.searchTechStacksByKeyword(pageable, keyword)).willReturn(pages);

		Integer currentPage = pageable.getPageNumber() + 1;

		Integer totalPages = pages.getTotalPages();

		if (totalPages == 0)
			totalPages += 1;

		//when
		SearchTechStackResponse searchResults = searchService.searchTechStacks(pageable, keyword);

		//then
		assertThat(searchResults).isNotNull();
		assertThat(searchResults.pagination().totalPage()).isEqualTo(totalPages);
		assertThat(searchResults.pagination().pageable()).isEqualTo(pages.getSize());
		assertThat(searchResults.pagination().isEnd()).isEqualTo(currentPage.equals(totalPages));
		assertThat(searchResults.techStackList()).isNotNull();

		//verify
		verify(searchRepository, times(1)).searchTechStacksByKeyword(any(Pageable.class), anyString());
		}
	}