package com.kernelsquare.memberapi.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.kernelsquare.core.constants.SecurityConstants;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
					.allowedOrigins("https://kernelsquare.live", "http://dev.kernelsquare.live", "http://localhost:3000")
					.allowCredentials(true)
					.allowedHeaders("*")
					.allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS", "PATCH")
					.exposedHeaders(SecurityConstants.AUTHORIZATION_HEADER, "RefreshToken");
			}
		};
	}
}
