package com.kernelsquare.domainmysql.domain.member_answer_vote.repository;

import com.kernelsquare.core.common_response.error.code.MemberAnswerVoteErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.member_answer_vote.entity.MemberAnswerVote;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberAnswerVoteReaderImpl implements MemberAnswerVoteReader {
    private final MemberAnswerVoteRepository memberAnswerVoteRepository;

    @Override
    public MemberAnswerVote findByMemberIdAndAnswerId(Long memberId, Long answerId) {
        return memberAnswerVoteRepository.findByMemberIdAndAnswerId(memberId, answerId)
            .orElseThrow(() -> new BusinessException(MemberAnswerVoteErrorCode.MEMBER_ANSWER_VOTE_NOT_FOUND));
    }

    @Override
    public Boolean existsByMemberIdAndAnswerId(Long memberId, Long answerId) {
        return memberAnswerVoteRepository.existsByMemberIdAndAnswerId(memberId, answerId);
    }

    @Override
    public List<MemberAnswerVote> findAllByMemberId(Long memberId) {
        return memberAnswerVoteRepository.findAllByMemberId(memberId);
    }
}
