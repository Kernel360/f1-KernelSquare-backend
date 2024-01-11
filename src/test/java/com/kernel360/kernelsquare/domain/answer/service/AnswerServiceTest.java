package com.kernel360.kernelsquare.domain.answer.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.kernel360.kernelsquare.domain.level.entity.Level;
import com.kernel360.kernelsquare.domain.member_answer_vote.entity.MemberAnswerVote;
import com.kernel360.kernelsquare.domain.member_answer_vote.repository.MemberAnswerVoteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kernel360.kernelsquare.domain.answer.dto.CreateAnswerRequest;
import com.kernel360.kernelsquare.domain.answer.dto.FindAnswerResponse;
import com.kernel360.kernelsquare.domain.answer.dto.UpdateAnswerRequest;
import com.kernel360.kernelsquare.domain.answer.entity.Answer;
import com.kernel360.kernelsquare.domain.answer.repository.AnswerRepository;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.member.repository.MemberRepository;
import com.kernel360.kernelsquare.domain.question.entity.Question;
import com.kernel360.kernelsquare.domain.question.repository.QuestionRepository;
import com.kernel360.kernelsquare.global.common_response.error.code.AnswerErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@DisplayName("답변 서비스 통합 테스트")
@ExtendWith(MockitoExtension.class)
public class AnswerServiceTest {
	@InjectMocks
	private AnswerService answerService;
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
		List<FindAnswerResponse> newAnswerList = answerService.findAllAnswer(testQuestionId);

		//then
		assertThat(testAnswers.size()).isEqualTo(newAnswerList.size());

		//verify
		verify(answerRepository, times(1)).findAnswersByQuestionIdSortedByCreationDate(anyLong());
		verify(answerRepository, only()).findAnswersByQuestionIdSortedByCreationDate(anyLong());
		verify(memberAnswerVoteRepository, only()).findAllByMemberId(anyLong());
	}


	@Test
	@DisplayName("질문에 대한 답변 생성")
	void createAnswer() throws Exception {
		//given
		Long foundMemberId = 1L;

		Long foundQuestionId = 1L;

		Long foundTestAnswerId = 1L;

		Member foundMember = createTestMember(foundMemberId);
		Optional<Member> optionalFoundMember = Optional.of(foundMember);

		Question foundQuestion = createTestQuestion();
		Optional<Question> optionalFoundQuestion = Optional.of(foundQuestion);

		Answer foundAnswer = createTestAnswer(foundTestAnswerId, 1L, foundMember, foundQuestion);
		Optional<Answer> optionalFoundAnswer = Optional.of(foundAnswer);

		CreateAnswerRequest createAnswerRequest = CreateAnswerRequest
			.builder()
			.memberId(foundMemberId)
			.content(foundAnswer.getContent())
			.imageUrl(foundAnswer.getImageUrl())
			.build();

		doReturn(CreateAnswerRequest.toEntity(
			createAnswerRequest,
			foundQuestion,
			foundMember))
			.when(answerRepository)
			.save(any(Answer.class));

		doReturn(Optional.of(foundMember))
			.when(memberRepository)
			.findById(anyLong());

		doReturn(Optional.of(foundQuestion))
			.when(questionRepository)
			.findById(anyLong());

		doReturn(Optional.of(foundAnswer))
			.when(answerRepository)
			.findById(anyLong());

		//when
		Long newCreatedAnswerId = answerService.createAnswer(createAnswerRequest, foundQuestionId);
		Optional<Answer> optionalTestAnswer = answerRepository.findById(foundTestAnswerId);

		//then
		assertThat(optionalTestAnswer.get().getContent()).isEqualTo(createAnswerRequest.content());
		assertThat(optionalTestAnswer.get().getMember().getId()).isEqualTo(createAnswerRequest.memberId());
		assertThat(optionalTestAnswer.get().getImageUrl()).isEqualTo(createAnswerRequest.imageUrl());

		//verify
		verify(answerRepository, times(1)).save(any(Answer.class));
		verify(memberRepository, times(1)).findById(anyLong());
		verify(memberRepository, only()).findById(anyLong());
		verify(questionRepository, times(1)).findById(anyLong());
		verify(questionRepository, only()).findById(anyLong());
		verify(answerRepository, times(1)).findById(anyLong());
	}

	@Test
	@DisplayName("특정 답변 수정")
	void updateAnswer() throws Exception {
		//given
		Long testAnswerId = 1L;

		Long memberId = 1L;

		Member member = createTestMember(memberId);
		Question question = createTestQuestion();
		Answer foundAnswer = createTestAnswer(testAnswerId, 1L, member, question);
		Optional<Answer> optionalFoundAnswer = Optional.of(foundAnswer);

		UpdateAnswerRequest updateAnswerRequest = new UpdateAnswerRequest(
			"Test Updated Content",
			"Test Updated Image URL"
		);

		Answer updatedAnswer = Answer.builder()
			.member(member)
			.question(question)
			.imageUrl(updateAnswerRequest.imageUrl())
			.content(updateAnswerRequest.content())
			.voteCount(foundAnswer.getVoteCount())
			.build();

		Optional<Answer> optionalUpdatedAnswer = Optional.of(updatedAnswer);

		doReturn(optionalFoundAnswer)
			.when(answerRepository)
			.findById(anyLong());

		//when
		answerService.updateAnswer(updateAnswerRequest, testAnswerId);

		doReturn(optionalUpdatedAnswer)
			.when(answerRepository)
			.findById(anyLong());

		Optional<Answer> optionalTestAnswer = answerRepository.findById(testAnswerId);

		//then
		assertThat(optionalTestAnswer.get().getContent()).isEqualTo(updateAnswerRequest.content());
		assertThat(optionalTestAnswer.get().getImageUrl()).isEqualTo(updateAnswerRequest.imageUrl());

		//verify
		verify(answerRepository, times(2)).findById(anyLong());
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
