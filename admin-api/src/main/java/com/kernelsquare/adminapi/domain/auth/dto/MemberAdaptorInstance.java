package com.kernelsquare.adminapi.domain.auth.dto;

import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;
import lombok.Builder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Builder
public record MemberAdaptorInstance(
    Member member,
    Level level,
    List<SimpleGrantedAuthority> authorities
) {
    public static MemberAdaptorInstance of(Member member) {
        return MemberAdaptorInstance.builder()
            .member(member)
            .level(member.getLevel())
            .authorities(getAuthorities(member))
            .build();
    }

    private static List<SimpleGrantedAuthority> getAuthorities(Member member) {
        return member.getAuthorities().stream()
            .map(MemberAuthority::getAuthority)
            .map(authority ->
                new SimpleGrantedAuthority(authority.getAuthorityType().getDescription()))
            .toList();
    }
}
