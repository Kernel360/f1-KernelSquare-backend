package com.kernel360.kernelsquare.domain.member_answer_vote.repository;

import com.kernel360.kernelsquare.domain.member_answer_vote.entity.MemberAnswerVote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberAnswerVoteRepository extends JpaRepository<MemberAnswerVote, Long> {
    Optional<MemberAnswerVote> findByMemberIdAndAnswerId(Long memberId, Long answerId);
}
