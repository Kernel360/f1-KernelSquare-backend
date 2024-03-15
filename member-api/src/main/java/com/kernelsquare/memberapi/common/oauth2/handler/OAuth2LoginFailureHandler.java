package com.kernelsquare.memberapi.common.oauth2.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        String errorCode;
        String errorMessage;

        if (exception instanceof OAuth2AuthenticationException) {
            OAuth2Error error = ((OAuth2AuthenticationException) exception).getError();
            errorCode = error.getErrorCode();
            errorMessage = error.getDescription();
        } else {
            errorCode = "9999";
            errorMessage = "소셜 로그인 실패! 서버 로그를 확인해주세요.";
        }

        log.info("OAuth2 로그인에 실패했습니다. 에러 코드: {}, 에러 메시지: {}", errorCode, errorMessage);

        // develope
        String redirectUrl = "https://localhost:3000/oauth/github";
        // main
//        String redirectUrl = "https://kernelsquare.live/oauth/github";

        redirectUrl += "?errorCode=" + URLEncoder.encode(errorCode, StandardCharsets.UTF_8.toString()) + "&errorMessage=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString());

        response.sendRedirect(redirectUrl);
    }
}