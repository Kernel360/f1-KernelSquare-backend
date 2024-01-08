package com.kernel360.kernelsquare.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.kernel360.kernelsquare.global.constants.SecurityConstants;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
					.allowedOrigins("http://3.34.88.0:3000", "http://localhost:3000")
					.allowCredentials(true)
					.allowedHeaders("*")
					.allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS", "PATCH")
					.exposedHeaders(SecurityConstants.AUTHORIZATION_HEADER, "RefreshToken");
			}
		};
	}
}
