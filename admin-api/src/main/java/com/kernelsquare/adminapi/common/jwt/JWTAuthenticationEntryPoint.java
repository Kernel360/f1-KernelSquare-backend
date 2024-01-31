package com.kernelsquare.adminapi.common.jwt;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kernelsquare.core.common_response.error.code.AuthErrorCode;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {
		response.setStatus(AuthErrorCode.UNAUTHENTICATED.getStatus().value());
		response.setContentType("application/json;charset=UTF-8");

		ObjectMapper objectMapper = new ObjectMapper();
		String jsonResponse = objectMapper.writeValueAsString(
			Map.of("code", AuthErrorCode.UNAUTHENTICATED.getCode(),
				"msg", AuthErrorCode.UNAUTHENTICATED.getMsg())
		);

		byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
		response.getOutputStream().write(responseBytes);
		response.getOutputStream().flush();
	}
}
