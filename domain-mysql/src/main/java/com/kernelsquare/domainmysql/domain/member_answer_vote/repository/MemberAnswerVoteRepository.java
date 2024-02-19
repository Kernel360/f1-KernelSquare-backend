package com.kernelsquare.domainmysql.domain.member_answer_vote.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kernelsquare.domainmysql.domain.member_answer_vote.entity.MemberAnswerVote;

public interface MemberAnswerVoteRepository extends JpaRepository<MemberAnswerVote, Long> {
	@Query("SELECT v FROM MemberAnswerVote v WHERE v.member.id = :memberId AND v.answer.id = :answerId")
	Optional<MemberAnswerVote> findByMemberIdAndAnswerId(@Param("memberId") Long memberId,
		@Param("answerId") Long answerId);

	@Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM MemberAnswerVote a WHERE a.member.id = :memberId AND a.answer.id = :answerId")
	Boolean existsByMemberIdAndAnswerId(@Param("memberId") Long memberId, @Param("answerId") Long answerId);

	List<MemberAnswerVote> findAllByMemberId(Long memberId);
}
