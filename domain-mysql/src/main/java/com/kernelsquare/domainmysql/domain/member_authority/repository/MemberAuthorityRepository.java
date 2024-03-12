package com.kernelsquare.domainmysql.domain.member_authority.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;

public interface MemberAuthorityRepository extends JpaRepository<MemberAuthority, Long> {
    void deleteAllByMemberId(Long memberId);
}