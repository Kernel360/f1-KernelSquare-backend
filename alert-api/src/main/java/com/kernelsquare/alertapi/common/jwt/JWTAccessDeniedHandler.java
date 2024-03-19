package com.kernelsquare.alertapi.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kernelsquare.core.common_response.error.code.AuthErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class JWTAccessDeniedHandler implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException, ServletException {
		response.setStatus(AuthErrorCode.UNAUTHORIZED_ACCESS.getStatus().value());
		response.setContentType("application/json;charset=UTF-8");

		ObjectMapper objectMapper = new ObjectMapper();
		String jsonResponse = objectMapper.writeValueAsString(
			Map.of("code", AuthErrorCode.UNAUTHORIZED_ACCESS.getCode(),
				"msg", AuthErrorCode.UNAUTHORIZED_ACCESS.getMsg())
		);

		byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
		response.getOutputStream().write(responseBytes);
		response.getOutputStream().flush();
	}
}
