package com.kernel360.kernelsquare.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kernel360.kernelsquare.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
