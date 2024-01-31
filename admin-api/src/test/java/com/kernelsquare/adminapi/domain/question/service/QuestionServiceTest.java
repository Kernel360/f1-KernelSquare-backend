package com.kernelsquare.adminapi.domain.question.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

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

import com.kernelsquare.adminapi.domain.question.dto.FindQuestionResponse;
import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.domainmysql.domain.question.repository.QuestionRepository;
import com.kernelsquare.domainmysql.domain.question_tech_stack.repository.QuestionTechStackRepository;

@DisplayName("질문 서비스 통합 테스트")
@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {
	@InjectMocks
	private QuestionService questionService;
	@Mock
	private QuestionRepository questionRepository;
	@Mock
	private MemberRepository memberRepository;
	@Mock
	private QuestionTechStackRepository questionTechStackRepository;

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
			.experience(0L)
			.introduction("hi, i'm hongjugwang.")
			.imageUrl("s3:qwe12fasdawczx")
			.level(level)
			.build();
	}

	private Level createTestLevel() {
		return Level.builder()
			.name(6L)
			.imageUrl("1.jpg")
			.levelUpperLimit(500L)
			.build();
	}

	@BeforeEach
	void setUp() {
		level = createTestLevel();
		member = createTestMember();
	}

	@Test
	@DisplayName("질문 조회 테스트")
	void testFindQuestion() {
		//given
		Question question = createTestQuestion(1L);

		given(questionRepository.findById(anyLong())).willReturn(Optional.ofNullable(question));

		//when
		FindQuestionResponse findQuestionResponse = questionService.findQuestion(question.getId());

		//then
		assertThat(findQuestionResponse).isNotNull();
		assertThat(findQuestionResponse.id()).isEqualTo(question.getId());
		assertThat(findQuestionResponse.title()).isEqualTo(question.getTitle());
		assertThat(findQuestionResponse.content()).isEqualTo(question.getContent());
		assertThat(findQuestionResponse.questionImageUrl().length()).isGreaterThan(question.getImageUrl().length());
		assertThat(findQuestionResponse.questionImageUrl()).endsWith(question.getImageUrl());
		assertThat(findQuestionResponse.nickname()).isEqualTo(member.getNickname());
		assertThat(findQuestionResponse.memberImageUrl().length()).isGreaterThan(member.getImageUrl().length());
		assertThat(findQuestionResponse.memberImageUrl()).endsWith(member.getImageUrl());
		assertThat(findQuestionResponse.level()).isEqualTo(member.getLevel().getName());
		assertThat(findQuestionResponse.levelImageUrl().length()).isGreaterThan(
			member.getLevel().getImageUrl().length());
		assertThat(findQuestionResponse.levelImageUrl()).endsWith(member.getLevel().getImageUrl());
		assertThat(findQuestionResponse.skills()).isEqualTo(question.getTechStackList()
			.stream().map(x -> x.getTechStack().getSkill()).toList());
		//ToDo 답변에 대한 로직이 구현된 후 해당 질문에 대한 답변 list가 잘담기는지 테스트해야 하는지 생각해볼 필요가 있음

		//verify
		verify(questionRepository, times(1)).findById(anyLong());
	}

	@Test
	@DisplayName("모든 질문 조회 테스트")
	void testFindAllQuestions() {
		//given
		Question question1 = createTestQuestion(1L);
		Question question2 = createTestQuestion(2L);
		List<Question> questions = List.of(question1, question2);

		Pageable pageable = PageRequest.of(0, 2);
		Page<Question> pages = new PageImpl<>(questions, pageable, questions.size());

		given(questionRepository.findById(question1.getId())).willReturn(Optional.of(question1));

		given(questionRepository.findById(question2.getId())).willReturn(Optional.of(question2));

		given(questionRepository.findAll(any(PageRequest.class))).willReturn(pages);

		Integer currentPage = pageable.getPageNumber() + 1;

		Integer totalPages = pages.getTotalPages();

		if (totalPages == 0)
			totalPages += 1;

		//when
		PageResponse<FindQuestionResponse> pageResponse = questionService.findAllQuestions(pageable);

		//then
		assertThat(pageResponse).isNotNull();
		assertThat(pageResponse.pagination().totalPage()).isEqualTo(totalPages);
		assertThat(pageResponse.pagination().pageable()).isEqualTo(pages.getSize());
		assertThat(pageResponse.pagination().isEnd()).isEqualTo(currentPage.equals(totalPages));
		assertThat(pageResponse.list()).isNotNull();

		//verify
		verify(questionRepository, times(1)).findAll(any(PageRequest.class));
	}

	@Test
	@DisplayName("질문 삭제 테스트")
	void testDeleteQuestion() {
		//given
		Question question = createTestQuestion(1L);

		given(questionRepository.findById(anyLong())).willReturn(Optional.ofNullable(question));

		doNothing()
			.when(questionRepository)
			.deleteById(anyLong());

		doNothing()
			.when(questionTechStackRepository)
			.deleteAllByQuestionId(anyLong());

		//when
		questionService.deleteQuestion(question.getId());

		//then
		verify(questionRepository, times(1)).deleteById(anyLong());
	}
}