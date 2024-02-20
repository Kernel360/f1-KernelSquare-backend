package com.kernelsquare.memberapi.domain.member_answer_vote.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kernelsquare.memberapi.domain.member_answer_vote.dto.CreateMemberAnswerVoteRequest;
import java.util.Objects;
import java.util.Optional;

import com.kernelsquare.core.common_response.error.code.AnswerErrorCode;
import com.kernelsquare.core.common_response.error.code.MemberAnswerVoteErrorCode;
import com.kernelsquare.core.common_response.error.code.MemberErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.answer.entity.Answer;
import com.kernelsquare.domainmysql.domain.answer.repository.AnswerRepository;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;
import com.kernelsquare.domainmysql.domain.member_answer_vote.entity.MemberAnswerVote;
import com.kernelsquare.domainmysql.domain.member_answer_vote.repository.MemberAnswerVoteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberAnswerVoteService {
	private final MemberRepository memberRepository;
	private final AnswerRepository answerRepository;
	private final MemberAnswerVoteRepository memberAnswerVoteRepository;

	@Transactional
	public void createVote(CreateMemberAnswerVoteRequest createMemberAnswerVoteRequest, Long answerId) {
		Member member = memberRepository.findById(createMemberAnswerVoteRequest.memberId())
			.orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

		Answer answer = answerRepository.findById(answerId)
			.orElseThrow(() -> new BusinessException(AnswerErrorCode.ANSWER_NOT_FOUND));

		if(Objects.equals(answer.getMember().getId(), createMemberAnswerVoteRequest.memberId())) {
			throw new BusinessException(MemberAnswerVoteErrorCode.MEMBER_ANSWER_VOTE_SELF_IMPOSSIBLE);
		}

		if(memberAnswerVoteRepository.existsByMemberIdAndAnswerId(createMemberAnswerVoteRequest.memberId(), answerId)) {
			throw new BusinessException(MemberAnswerVoteErrorCode.MEMBER_ANSWER_VOTE_DUPLICATION);
		}

		MemberAnswerVote memberAnswerVote = CreateMemberAnswerVoteRequest.toEntity(
			createMemberAnswerVoteRequest, member, answer
		);
		memberAnswerVoteRepository.save(memberAnswerVote);

		if ((long)createMemberAnswerVoteRequest.status() == 1) {
			answerRepository.upVoteAnswer(answerId);
			return;
		}
		answerRepository.downVoteAnswer(answerId);
	}

	@Transactional
	public void deleteVote(Long answerId) {
		Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
		MemberAnswerVote memberAnswerVote = memberAnswerVoteRepository.findByMemberIdAndAnswerId(memberId, answerId)
			.orElseThrow(() -> new BusinessException(MemberAnswerVoteErrorCode.MEMBER_ANSWER_VOTE_NOT_FOUND));

		memberAnswerVoteRepository.deleteById(memberAnswerVote.getId());

		int memberAnswerVoteStatus = -memberAnswerVote.getStatus();
		if (memberAnswerVoteStatus == 1) {
			answerRepository.upVoteAnswer(answerId);
			return;
		}
		answerRepository.downVoteAnswer(answerId);
	}
}
