package com.kernel360.kernelsquare.global.jwt;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 필요한 권한이 존재하지 않을 경우 403 Forbidden 에러를 반환하는 클래스
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
	// todo : 실제 프론트 서버에서 에러 처리로 가도록 바꾸어 함
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException, ServletException {
		// response.setStatus(HttpStatus.FORBIDDEN.value());
		// response.setContentType("application/json;charset=UTF-8");
		//
		// ObjectMapper objectMapper = new ObjectMapper();
		// String jsonResponse = objectMapper.writeValueAsString(
		// 	Map.of("error", "Access Denied", "message", "필요한 권한이 없습니다.")
		// );
		//
		// byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
		// response.getOutputStream().write(responseBytes);
		// response.getOutputStream().flush();
	}
}
