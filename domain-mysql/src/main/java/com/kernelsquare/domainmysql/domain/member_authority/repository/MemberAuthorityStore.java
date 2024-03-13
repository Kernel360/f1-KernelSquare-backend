package com.kernelsquare.domainmysql.domain.member_authority.repository;

import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;

public interface MemberAuthorityStore {
    MemberAuthority store(MemberAuthority memberAuthority);
    void deleteMemberAuthorities(Long memberId);
}
