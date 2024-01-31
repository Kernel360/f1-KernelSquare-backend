package com.kernelsquare.adminapi.domain.answer.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.kernelsquare.adminapi.domain.answer.dto.FindAllAnswerResponse;
import com.kernelsquare.core.common_response.error.code.AnswerErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.answer.entity.Answer;
import com.kernelsquare.domainmysql.domain.answer.repository.AnswerRepository;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.level.repository.LevelRepository;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;
import com.kernelsquare.domainmysql.domain.member_answer_vote.entity.MemberAnswerVote;
import com.kernelsquare.domainmysql.domain.member_answer_vote.repository.MemberAnswerVoteRepository;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.domainmysql.domain.question.repository.QuestionRepository;

@DisplayName("답변 서비스 통합 테스트")
@ExtendWith(MockitoExtension.class)
public class AnswerServiceTest {
	@InjectMocks
	private AnswerService answerService;
	@Mock
	private LevelRepository levelRepository;
	@Mock
	private AnswerRepository answerRepository;
	@Mock
	private MemberRepository memberRepository;
	@Mock
	private QuestionRepository questionRepository;
	@Mock
	private MemberAnswerVoteRepository memberAnswerVoteRepository;

	@Test
	@DisplayName("질문에 대한 답변 조회")
	void findAllAnswer() throws Exception {
		//given
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		SecurityContextHolder.setContext(securityContext);
		Authentication authentication = new UsernamePasswordAuthenticationToken(
			"0", null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

		given(securityContext.getAuthentication()).willReturn(authentication);

		Long testQuestionId = 1L;
		List<Answer> testAnswers = new ArrayList<>();
		List<MemberAnswerVote> testVotes = new ArrayList<>();

		Level level = createTestLevel(0L, 1L);
		Question question = createTestQuestion();
		Member member = createTestMemberWithLevel(0L, level);
		Answer answer = createTestAnswer(0L, 0L, member, question);
		testAnswers.add(answer);

		MemberAnswerVote memberAnswerVote = createTestVote(0L, 1, member, answer);
		testVotes.add(memberAnswerVote);

		doReturn(testAnswers)
			.when(answerRepository)
			.findAnswersByQuestionIdSortedByCreationDate(anyLong());

		doReturn(testVotes)
			.when(memberAnswerVoteRepository)
			.findAllByMemberId(anyLong());

		//when
		FindAllAnswerResponse newAnswerList = answerService.findAllAnswer(testQuestionId);

		//then
		assertThat(testAnswers.size()).isEqualTo(newAnswerList.answerResponses().size());

		//verify
		verify(answerRepository, times(1)).findAnswersByQuestionIdSortedByCreationDate(anyLong());
		verify(answerRepository, only()).findAnswersByQuestionIdSortedByCreationDate(anyLong());
		verify(memberAnswerVoteRepository, only()).findAllByMemberId(anyLong());
	}

	@Test
	@DisplayName("특정 답변 삭제")
	void deleteAnswer() throws Exception {
		//when
		Long testAnswerId = 1L;

		Long memberId = 1L;

		Member member = createTestMember(memberId);
		Question question = createTestQuestion();

		doNothing()
			.when(answerRepository)
			.deleteById(anyLong());

		doThrow(new BusinessException(AnswerErrorCode.ANSWER_NOT_FOUND))
			.when(answerRepository)
			.findById(anyLong());

		//when
		answerService.deleteAnswer(testAnswerId);

		//then
		assertThatThrownBy(() -> answerRepository.findById(anyLong()))
			.isExactlyInstanceOf(BusinessException.class);

		//verify
		verify(answerRepository, times(1)).deleteById(anyLong());
		verify(answerRepository, times(1)).findById(anyLong());
	}

	private Question createTestQuestion() {
		return Question
			.builder()
			.title("Test Question")
			.content("Test Content")
			.imageUrl("S3:TestImage")
			.closedStatus(false)
			.build();
	}

	private Member createTestMemberWithLevel(Long memberId, Level level) {
		return Member
			.builder()
			.id(memberId)
			.nickname("hongjugwang" + memberId)
			.email("jugwang" + memberId + "@naver.com")
			.password("hashedPassword")
			.experience(10000L)
			.introduction("hi, i'm hongjugwang.")
			.level(level)
			.imageUrl("s3:qwe12fasdawczx")
			.build();
	}

	private Member createTestMember(Long memberId) {
		return Member
			.builder()
			.id(memberId)
			.nickname("hongjugwang" + memberId)
			.email("jugwang" + memberId + "@naver.com")
			.password("hashedPassword")
			.experience(10000L)
			.introduction("hi, i'm hongjugwang.")
			.imageUrl("s3:qwe12fasdawczx")
			.build();
	}

	private Answer createTestAnswer(Long answerId, Long index, Member member, Question question) {
		return Answer
			.builder()
			.id(answerId)
			.content("Test Answer" + index)
			.voteCount(index)
			.imageUrl("S3:TestAnswer" + index)
			.member(member)
			.question(question)
			.build();
	}

	private MemberAnswerVote createTestVote(Long voteId, int status, Member member, Answer answer) {
		return MemberAnswerVote
			.builder()
			.id(voteId)
			.status(status)
			.member(member)
			.answer(answer)
			.build();
	}

	private Level createTestLevel(Long levelId, Long levelName) {
		return Level
			.builder()
			.id(levelId)
			.name(levelName)
			.imageUrl("S3:TestLevelImage")
			.levelUpperLimit(500L)
			.build();
	}
}
