package com.kernelsquare.adminapi.domain.auth.validation;

import com.kernelsquare.core.common_response.error.code.AuthErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.type.AuthorityType;
import com.kernelsquare.domainmysql.domain.authority.entity.Authority;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public class AuthValidation {
    public static void validatePassword(PasswordEncoder passwordEncoder, String requestPassword, String MemberPassword) {
        if (!passwordEncoder.matches(requestPassword, MemberPassword)) {
            throw new BusinessException(AuthErrorCode.INVALID_PASSWORD);
        }
    }

    public static void validateAdminAuthority(Member member) {
        List<AuthorityType> findMemberAuthorities = member.getAuthorities().stream()
            .map(MemberAuthority::getAuthority)
            .map(Authority::getAuthorityType)
            .toList();

        if (!findMemberAuthorities.contains(AuthorityType.ROLE_ADMIN)) {
            throw new BusinessException(AuthErrorCode.UNAUTHORIZED_ACCESS);
        }
    }
}
