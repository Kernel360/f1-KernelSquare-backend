package com.kernelsquare.memberapi.domain.auth.validation;

import com.kernelsquare.core.common_response.error.code.AuthErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthValidation {
    public static void validatePassword(String loginPassword, String memberPassword, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(loginPassword, memberPassword)) {
            throw new BusinessException(AuthErrorCode.INVALID_PASSWORD);
        }
    }
}
