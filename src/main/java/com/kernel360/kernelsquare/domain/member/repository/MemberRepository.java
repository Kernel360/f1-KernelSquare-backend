package com.kernel360.kernelsquare.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kernel360.kernelsquare.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);

	Boolean existsByEmail(String email);

	Boolean existsByNickname(String nickname);
}
