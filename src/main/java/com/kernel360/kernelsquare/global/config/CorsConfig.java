package com.kernel360.kernelsquare.global.config;

import java.util.Collections;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

public class CorsConfig implements CorsConfigurationSource {
	@Override
	public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
		CorsConfiguration config = new CorsConfiguration();
		// todo: Next.js 기본 포트로 설정 (3000) 추후 변경된 포트 번호로 수정해야함
		config.setAllowedOrigins(Collections.singletonList(("http://localhost:3000")));
		// 어떤 Http Method를 허용할건지? "*"는 모든 method 허용
		config.setAllowedMethods((Collections.singletonList("*")));
		config.setAllowCredentials(true);
		config.setAllowedHeaders(Collections.singletonList("*"));
		config.setMaxAge(3600L);
		return config;
	}
}
