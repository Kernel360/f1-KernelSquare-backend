package com.kernelsquare.memberapi.domain.auth.validation;

import com.kernelsquare.core.common_response.error.code.AuthErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthValidation {
    private final PasswordEncoder passwordEncoder;
    public void validatePassword(String loginPassword, String memberPassword) {
        if (!passwordEncoder.matches(loginPassword, memberPassword)) {
            throw new BusinessException(AuthErrorCode.INVALID_PASSWORD);
        }
    }
}
