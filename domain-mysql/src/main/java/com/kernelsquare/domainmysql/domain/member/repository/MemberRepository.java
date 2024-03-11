package com.kernelsquare.domainmysql.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kernelsquare.domainmysql.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);

	Optional<Member> findByNickname(String nickname);

	Boolean existsByEmail(String email);

	Boolean existsByNickname(String nickname);
}
