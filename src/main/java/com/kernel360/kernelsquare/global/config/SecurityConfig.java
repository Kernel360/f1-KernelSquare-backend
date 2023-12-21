package com.kernel360.kernelsquare.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/* todo : securityMatcher 내부의 API endpoint를 세부적으로 명시해야할까요?
	    추후에 비회원 기능을 추가하려면 세부적으로 명시하는게 나아보이긴 함
	*	1. 	.securityMatcher(
				"/api/v1/auth/isEmailUnique",
				"/api/v1/auth/isNicknameUnique",
				"/api/v1/auth/authenticate",
				"/api/v1/auth/logout",
				"/api/v1/auth/signup",
				"/api/v1/auth/socialSignUp",
				"/api/v1/auth/callback",
				"/api/v1/auth/reissue")

		2. .securityMatcher("/api/v1/auth/**")
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.cors(corsConfigurer -> corsConfigurer.configurationSource(new CorsConfig()))
			.csrf(AbstractHttpConfigurer::disable)
			.securityMatcher(
				"/api/v1/auth/**",
				"/api/v1/members/**")
			.authorizeHttpRequests(authz -> authz
				.requestMatchers("/api/v1/auth/**", "/api/v1/members/**").permitAll()
				// .requestMatchers("/api/v1/members/**").hasAnyRole("ADMIN", "MEMBER", "MENTOR")
				.anyRequest().authenticated()
			)

			// .exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer
			// 	.authenticationEntryPoint(jwtAuthenticationEntryPoint)
			// 	.accessDeniedHandler(jwtAccessDeniedHandler))

			.sessionManagement(sessionManagementConfigurer ->
				sessionManagementConfigurer
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

			// .with(new JwtSecurityConfig(tokenProvider))
			//
			// .oauth2Login(oAuth2LoginConfigurer ->
			// 	oAuth2LoginConfigurer
			// 		.successHandler(oAuth2LoginSuccessHandler)
			// 		.failureHandler(oAuth2LoginFailureHandler)
			// 		.userInfoEndpoint(userInfoEndpointConfigurer ->
			// 			userInfoEndpointConfigurer.userService(customOAuth2MemberService)))
			.logout(Customizer.withDefaults());

		return http.build();
	}

	// todo : 어떤 API를 비회원에게 보게 할건지? 질문 같은거가 있을 수 있음
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring()
			.requestMatchers("/api/v1/question");
	}
}
