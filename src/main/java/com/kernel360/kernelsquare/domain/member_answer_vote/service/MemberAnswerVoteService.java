package com.kernel360.kernelsquare.domain.member_answer_vote.service;

import com.kernel360.kernelsquare.domain.answer.entity.Answer;
import com.kernel360.kernelsquare.domain.answer.repository.AnswerRepository;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.member.repository.MemberRepository;
import com.kernel360.kernelsquare.domain.member_answer_vote.dto.CreateMemberAnswerVoteRequest;
import com.kernel360.kernelsquare.domain.member_answer_vote.entity.MemberAnswerVote;
import com.kernel360.kernelsquare.domain.member_answer_vote.repository.MemberAnswerVoteRepository;
import com.kernel360.kernelsquare.global.common_response.error.code.AnswerErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.code.MemberAnswerVoteErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.code.MemberErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberAnswerVoteService {
    private final MemberRepository memberRepository;
    private final AnswerRepository answerRepository;
    private final MemberAnswerVoteRepository memberAnswerVoteRepository;

    @Transactional
    public void updateVoteCount(Long diffVoteCount, Long answerId) {
        if (diffVoteCount == 1) {
            answerRepository.upVoteAnswer(answerId);
            return;
        }
        answerRepository.downVoteAnswer(answerId);
    }

    @Transactional
    public void createVote(CreateMemberAnswerVoteRequest createMemberAnswerVoteRequest, Long answerId) {
        Member member = memberRepository.findById(createMemberAnswerVoteRequest.memberId())
                .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new BusinessException(AnswerErrorCode.ANSWER_NOT_FOUND));

        MemberAnswerVote memberAnswerVote = CreateMemberAnswerVoteRequest.toEntity(
                createMemberAnswerVoteRequest, member, answer
        );
        memberAnswerVoteRepository.save(memberAnswerVote);
        updateVoteCount((long) createMemberAnswerVoteRequest.status(), answerId);
    }

    @Transactional
    public void deleteVote(Long answerId) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

        MemberAnswerVote memberAnswerVote = memberAnswerVoteRepository.findByMemberIdAndAnswerId(memberId, answerId)
                .orElseThrow(() -> new BusinessException(MemberAnswerVoteErrorCode.MEMBER_ANSWER_VOTE_NOT_FOUND));

        int memberAnswerVoteStatus = memberAnswerVote.getStatus();
        updateVoteCount((long) -memberAnswerVoteStatus, answerId);
        memberAnswerVoteRepository.deleteById(memberAnswerVote.getId());
    }
}
