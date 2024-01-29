package com.kernel360.kernelsquare.domain.member_answer_vote.repository;

import com.kernel360.kernelsquare.domain.member_answer_vote.entity.MemberAnswerVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberAnswerVoteRepository extends JpaRepository<MemberAnswerVote, Long> {
    @Query("SELECT v FROM member_answer_vote v WHERE v.member.id = :memberId AND v.answer.id = :answerId")
    Optional<MemberAnswerVote> findByMemberIdAndAnswerId(@Param("memberId") Long memberId, @Param("answerId") Long answerId);
    List<MemberAnswerVote> findAllByMemberId(Long memberId);
}
