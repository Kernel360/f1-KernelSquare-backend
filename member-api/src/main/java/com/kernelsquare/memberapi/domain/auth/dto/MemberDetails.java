package com.kernelsquare.memberapi.domain.auth.dto;

import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class MemberDetails implements UserDetails, OAuth2User, OidcUser {
    private final Member member;
    private final Level level;
    private final List<SimpleGrantedAuthority> authorities;
    private Map<String, Object> attributes;

    /* 일반 로그인 */
    public MemberDetails(MemberAdaptorInstance memberInstance) {
        this.member = memberInstance.member();
        this.level = memberInstance.level();
        this.authorities = memberInstance.authorities();
    }

    public MemberDetails(MemberAdaptorInstance memberInstance, Map<String, Object> attributes) {
        this.member = memberInstance.member();
        this.level = memberInstance.level();
        this.authorities = memberInstance.authorities();
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
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

    @Override
    public Map<String, Object> getClaims() {
        return null;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }

    @Override
    public String getName() {
        return String.valueOf(member.getId());
    }

    public static MemberDetails create(MemberAdaptorInstance member, Map<String, Object> attributes) {
        return new MemberDetails(
            member,
            attributes
        );
    }
}
