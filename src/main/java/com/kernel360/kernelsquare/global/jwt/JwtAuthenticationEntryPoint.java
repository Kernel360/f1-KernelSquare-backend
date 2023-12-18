package com.kernel360.kernelsquare.global.jwt;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 유효한 자격증명을 제공하지 않고 접근할 때 401 Unauthorized 에러를 반환하는 클래스
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	// todo : 실제 프론트 서버에서 에러 처리로 가도록 바꾸어 함

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {
		response.sendRedirect("/error/pages-error-404.html");
	}
}
