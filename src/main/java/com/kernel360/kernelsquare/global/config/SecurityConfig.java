package com.kernel360.kernelsquare.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.kernel360.kernelsquare.global.jwt.JwtAccessDeniedHandler;
import com.kernel360.kernelsquare.global.jwt.JwtAuthenticationEntryPoint;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

		http.authorizeHttpRequests(authz -> authz
			.requestMatchers("/api/v1/questions/**",
				"/api/v1/auth/**").permitAll()
			.requestMatchers("/api/v1/members/**").hasAnyRole("ADMIN", "MEMBER", "MENTOR")
		);

		http.exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer
			.authenticationEntryPoint(jwtAuthenticationEntryPoint)
			.accessDeniedHandler(jwtAccessDeniedHandler));

		http.sessionManagement(sessionManagementConfigurer ->
			sessionManagementConfigurer
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// http.oauth2Login(oAuth2LoginConfigurer ->
		// 		oAuth2LoginConfigurer
		// 			.successHandler(oAuth2LoginSuccessHandler)
		// 			.failureHandler(oAuth2LoginFailureHandler)
		// 			.userInfoEndpoint(userInfoEndpointConfigurer ->
		// 				userInfoEndpointConfigurer.userService(customOAuth2MemberService)))
		http.logout(Customizer.withDefaults());

		return http.build();
	}
}
