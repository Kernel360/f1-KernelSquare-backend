package com.kernelsquare.memberapi.domain.member_answer_vote.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.eq;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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

import com.kernelsquare.memberapi.domain.member_answer_vote.dto.CreateMemberAnswerVoteRequest;
import com.kernelsquare.domainmysql.domain.answer.entity.Answer;
import com.kernelsquare.domainmysql.domain.answer.repository.AnswerRepository;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;
import com.kernelsquare.domainmysql.domain.member_answer_vote.entity.MemberAnswerVote;
import com.kernelsquare.domainmysql.domain.member_answer_vote.repository.MemberAnswerVoteRepository;
import com.kernelsquare.domainmysql.domain.question.entity.Question;

@DisplayName("투표 서비스 통합 테스트")
@ExtendWith(MockitoExtension.class)
public class MemberAnswerVoteServiceTest {
	@InjectMocks
	private MemberAnswerVoteService memberAnswerVoteService;
	@Mock
	private MemberRepository memberRepository;
	@Mock
	private AnswerRepository answerRepository;
	@Mock
	private MemberAnswerVoteRepository memberAnswerVoteRepository;

	private Member member;
	private Answer answer;
	private Question question;
	private MemberAnswerVote memberAnswerVote;
	private MemberAnswerVote memberAnswerVoteWithId;

	private CreateMemberAnswerVoteRequest createMemberAnswerVoteRequest;

	@BeforeEach
	void setUp() {
		member = createTestMember();
		question = createTestQuestion();
		answer = createTestAnswer(member, question);
		memberAnswerVote = createTestVote(member, answer);
		memberAnswerVoteWithId = createTestVoteWithId(answer);
		createMemberAnswerVoteRequest = new CreateMemberAnswerVoteRequest(1L, 1);
	}

	@Test
	@DisplayName("투표 생성 테스트")
	void testCreateVote() {
		//given
		given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(member));
		given(answerRepository.findById(anyLong())).willReturn(Optional.ofNullable(answer));
		given(memberAnswerVoteRepository.save(any(MemberAnswerVote.class))).willReturn(memberAnswerVote);

		//when
		memberAnswerVoteService.createVote(createMemberAnswerVoteRequest, 1L);

		//verify
		verify(memberRepository, times(1)).findById(anyLong());
		verify(answerRepository, times(1)).findById(anyLong());
		verify(memberAnswerVoteRepository, times(1)).save(any(MemberAnswerVote.class));
	}

	@Test
	@DisplayName("투표 삭제 테스트")
	void testDeleteVote() {
		//given
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		SecurityContextHolder.setContext(securityContext);
		Authentication authentication = new UsernamePasswordAuthenticationToken(
			"0", null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

		given(securityContext.getAuthentication()).willReturn(authentication);
		given(memberAnswerVoteRepository.findByMemberIdAndAnswerId(eq(0L), anyLong()))
			.willReturn(Optional.ofNullable(memberAnswerVoteWithId));
		doNothing().when(memberAnswerVoteRepository).deleteById(memberAnswerVoteWithId.getId());

		//when
		memberAnswerVoteService.deleteVote(0L);

		//verify
		verify(memberAnswerVoteRepository, times(1)).findByMemberIdAndAnswerId(eq(0L), anyLong());
		verify(memberAnswerVoteRepository, times(1)).deleteById(memberAnswerVoteWithId.getId());
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

	private Member createTestMember() {
		return Member
			.builder()
			.nickname("hongjugwang")
			.email("jugwang@naver.com")
			.password("hashedPassword")
			.experience(10000L)
			.introduction("hi, i'm hongjugwang.")
			.imageUrl("s3:qwe12fasdawczx")
			.build();
	}

	private Answer createTestAnswer(Member member, Question question) {
		return Answer
			.builder()
			.content("Test Answer")
			.voteCount(1L)
			.imageUrl("S3:TestAnswer")
			.member(member)
			.question(question)
			.build();
	}

	private MemberAnswerVote createTestVote(Member member, Answer answer) {
		return MemberAnswerVote
			.builder()
			.member(member)
			.answer(answer)
			.status(1)
			.build();
	}

	private MemberAnswerVote createTestVoteWithId(Answer answer) {
		return MemberAnswerVote
			.builder()
			.id(0L)
			.answer(answer)
			.status(1)
			.build();
	}
}
