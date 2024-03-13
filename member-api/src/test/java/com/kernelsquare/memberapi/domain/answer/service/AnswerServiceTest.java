package com.kernelsquare.memberapi.domain.answer.service;

import com.kernelsquare.domainmysql.domain.answer.command.AnswerCommand;
import com.kernelsquare.domainmysql.domain.answer.entity.Answer;
import com.kernelsquare.domainmysql.domain.answer.info.AnswerInfo;
import com.kernelsquare.domainmysql.domain.answer.repository.AnswerReader;
import com.kernelsquare.domainmysql.domain.answer.repository.AnswerRepository;
import com.kernelsquare.domainmysql.domain.answer.repository.AnswerStore;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.level.repository.LevelReader;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member_answer_vote.entity.MemberAnswerVote;
import com.kernelsquare.domainmysql.domain.member_answer_vote.repository.MemberAnswerVoteReader;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.domainmysql.domain.question.repository.QuestionReader;
import com.kernelsquare.memberapi.domain.answer.dto.FindAllAnswerResponse;
import com.kernelsquare.memberapi.domain.answer.dto.UpdateAnswerRequest;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdaptorInstance;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@DisplayName("답변 서비스 통합 테스트")
@ExtendWith(MockitoExtension.class)
public class AnswerServiceTest {
	@InjectMocks
	private AnswerService answerService;
	@Mock
	private QuestionReader questionReader;
	@Mock
	private AnswerStore answerStore;
	@Mock
	private LevelReader levelReader;
	@Mock
	private MemberAnswerVoteReader memberAnswerVoteReader;
	@Mock
	private AnswerReader answerReader;

	private Member member;

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
			.when(answerReader)
			.findAnswers(anyLong());

		doReturn(testVotes)
			.when(memberAnswerVoteReader)
			.findAllByMemberId(anyLong());

		//when
		FindAllAnswerResponse newAnswerList = answerService.findAllAnswer(testQuestionId);

		//then
		assertThat(testAnswers.size()).isEqualTo(newAnswerList.answerResponses().size());

		//verify
		verify(answerReader, times(1)).findAnswers(anyLong());
		verify(answerReader, only()).findAnswers(anyLong());
		verify(memberAnswerVoteReader, only()).findAllByMemberId(anyLong());
	}

	@Test
	@DisplayName("질문에 대한 답변 생성")
	void createAnswer() throws Exception {
		//given
		Long foundMemberId = 1L;

		Long foundQuestionId = 1L;

		Long foundTestAnswerId = 1L;

		Long memberId = 2L; // 본인 질문에 답변 달 수 없기에 foundMemberId 와 다르게 설정
		member = createTestMember(memberId);

		Question foundQuestion = createTestQuestion();

		Level foundLevel = createTestLevel(0L, 1L);

		Member foundMember = createTestMember(foundMemberId);
		foundMember.updateLevel(foundLevel);

		Answer foundAnswer = createTestAnswer(foundTestAnswerId, 1L, foundMember, foundQuestion);

		AnswerCommand.CreateAnswer command = AnswerCommand.CreateAnswer.builder()
			.questionId(foundQuestionId)
			.content(foundAnswer.getContent())
			.imageUrl(foundAnswer.getImageUrl())
			.author(foundMember)
			.build();


		doReturn(foundQuestion).when(questionReader).findQuestion(anyLong());
		doReturn(foundAnswer).when(answerStore).store(any(Answer.class));
		doReturn(foundLevel).when(levelReader).findLevel(anyLong());

		//when
		AnswerInfo answerInfo = answerService.createAnswer(command);


		//then
		assertThat(answerInfo.getRecipientId()).isEqualTo(foundQuestion.getMember().getId().toString());
		assertThat(answerInfo.getRecipient()).isEqualTo(foundQuestion.getMember().getNickname());
		assertThat(answerInfo.getSenderId()).isEqualTo(foundAnswer.getMember().getId().toString());
		assertThat(answerInfo.getSender()).isEqualTo(foundAnswer.getMember().getNickname());
		assertThat(answerInfo.getQuestionTitle()).isEqualTo(foundQuestion.getTitle());

		//verify
		verify(questionReader, times(1)).findQuestion(anyLong());
		verify(answerStore, times(1)).store(any(Answer.class));
		verify(levelReader, times(1)).findLevel(anyLong());
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

		UpdateAnswerRequest updateAnswerRequest = new UpdateAnswerRequest(
			"Test Updated Content",
			"Test Updated Image URL"
		);

		doReturn(foundAnswer)
			.when(answerReader)
			.findAnswer(anyLong());

		MemberAdapter memberAdapter = new MemberAdapter(MemberAdaptorInstance.of(member));

		//when
		answerService.updateAnswer(updateAnswerRequest, testAnswerId, memberAdapter);

		//then
		assertThat(foundAnswer.getContent()).isEqualTo(updateAnswerRequest.content());
		assertThat(foundAnswer.getImageUrl()).isEqualTo(updateAnswerRequest.imageUrl());

		//verify
		verify(answerReader, times(1)).findAnswer(anyLong());
	}

	@Test
	@DisplayName("특정 답변 삭제")
	void deleteAnswer() throws Exception {
		//when
		Long testAnswerId = 1L;

		Long memberId = 1L;

		Member member = createTestMember(memberId);
		Question question = createTestQuestion();

		Answer foundAnswer = createTestAnswer(testAnswerId, 1L, member, question);

		doNothing()
			.when(answerStore)
			.delete(anyLong());

		doReturn(foundAnswer)
			.when(answerReader)
			.findAnswer(anyLong());

		MemberAdapter memberAdapter = new MemberAdapter(MemberAdaptorInstance.of(member));

		//when
		answerService.deleteAnswer(testAnswerId, memberAdapter);

		//verify
		verify(answerStore, times(1)).delete(anyLong());
		verify(answerReader, times(1)).findAnswer(anyLong());
	}

	private Question createTestQuestion() {
		return Question
			.builder()
			.title("Test Question")
			.content("Test Content")
			.imageUrl("S3:TestImage")
			.closedStatus(false)
			.member(member)
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
