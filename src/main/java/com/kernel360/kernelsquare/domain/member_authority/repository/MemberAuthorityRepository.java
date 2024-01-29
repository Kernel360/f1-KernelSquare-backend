package com.kernel360.kernelsquare.domain.member_authority.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kernel360.kernelsquare.domain.member_authority.entity.MemberAuthority;

public interface MemberAuthorityRepository extends JpaRepository<MemberAuthority, Long> {
}