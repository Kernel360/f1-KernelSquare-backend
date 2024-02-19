package com.kernelsquare.memberapi.domain.auth.dto;

import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class MemberDetails implements UserDetails {
    private Member member;

    /* 일반 로그인 */
    public MemberDetails(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities;
        authorities = member
			.getAuthorities()
			.stream()
			.map(MemberAuthority::getAuthority)
			.map(authority ->
				new SimpleGrantedAuthority(authority
					.getAuthorityType()
					.getDescription()))
			.toList();

        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    /* String 형식의 member_id */
    @Override
    public String getUsername() {
        return String.valueOf(member.getId());
    }

    /* 계정 만료 여부
     * true :  만료 안됨
     * false : 만료
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /* 계정 잠김 여부
     * true : 잠기지 않음
     * false : 잠김
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /* 비밀번호 만료 여부
     * true : 만료 안 됨
     * false : 만료
     */

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /* 사용자 활성화 여부
     * true : 활성화 됨
     * false : 활성화 안 됨
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
