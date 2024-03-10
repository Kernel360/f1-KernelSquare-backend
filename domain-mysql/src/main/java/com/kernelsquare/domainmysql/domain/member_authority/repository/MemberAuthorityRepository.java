package com.kernelsquare.domainmysql.domain.member_authority.repository;

import com.kernelsquare.domainmysql.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;

import java.util.List;

public interface MemberAuthorityRepository extends JpaRepository<MemberAuthority, Long> {
    List<MemberAuthority> findAllByMember(Member member);
}