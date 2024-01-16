package com.kernel360.kernelsquare.domain.member.repository;

import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kernel360.kernelsquare.domain.member.entity.Member;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);

	Boolean existsByEmail(String email);

	Boolean existsByNickname(String nickname);

	@Modifying
	@Query("UPDATE member m SET m.experience = :expValue WHERE m.id = :memberId")
	void updateExperienceByValue(@Param("memberId") Long memberId, @Param("expValue") Long expValue);
	@Modifying
	@Query("UPDATE member m SET m.experience = m.experience + :diffExp WHERE m.id = :memberId")
	void updateExperienceByDifference(@Param("memberId") Long memberId, @Param("diffExp") Long diffExp);
}
