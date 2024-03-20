package com.kernelsquare.alertapi.common.config;

import com.kernelsquare.alertapi.common.filter.JWTSettingFilter;
import com.kernelsquare.alertapi.common.jwt.JWTAccessDeniedHandler;
import com.kernelsquare.alertapi.common.jwt.JWTAuthenticationEntryPoint;
import com.kernelsquare.alertapi.domain.auth.service.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JWTAccessDeniedHandler jwtAccessDeniedHandler;
	private final TokenProvider tokenProvider;

	private final String[] permitAllPatterns = new String[] {
		"/actuator",
		"/actuator/**",
		"/docs/**",
	};

	private final String[] hasAnyAuthorityPatterns = new String[] {
		"/api/v1/alerts/**",
	};

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	//todo : filter 설정 추가하기
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
		http.authorizeHttpRequests(authz -> authz
			// 모든 접근 허용
			.requestMatchers(permitAllPatterns).permitAll()
			// 백엔드 임시 테스트 창
			.requestMatchers(HttpMethod.GET, "/api/v1/test").permitAll()

			// 모든 권한에 대한 접근 허용
			.requestMatchers(hasAnyAuthorityPatterns).authenticated()
		);

		http.addFilterBefore(new JWTSettingFilter(tokenProvider), BasicAuthenticationFilter.class);

		http.exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer
			.authenticationEntryPoint(jwtAuthenticationEntryPoint)
			.accessDeniedHandler(jwtAccessDeniedHandler));

		http.sessionManagement(sessionManagementConfigurer ->
			sessionManagementConfigurer
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.logout(Customizer.withDefaults());

		return http.build();
	}
}
