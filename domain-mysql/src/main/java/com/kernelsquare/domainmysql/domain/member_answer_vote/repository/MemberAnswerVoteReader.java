package com.kernelsquare.domainmysql.domain.member_answer_vote.repository;

import com.kernelsquare.domainmysql.domain.member_answer_vote.entity.MemberAnswerVote;

import java.util.List;

public interface MemberAnswerVoteReader {
    MemberAnswerVote findByMemberIdAndAnswerId(Long memberId, Long answerId);

    Boolean existsByMemberIdAndAnswerId(Long memberId, Long answerId);

    List<MemberAnswerVote> findAllByMemberId(Long memberId);
}
