package com.kernelsquare.memberapi.common.config;

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

import com.kernelsquare.memberapi.common.filter.JWTSettingFilter;
import com.kernelsquare.memberapi.common.jwt.JWTAccessDeniedHandler;
import com.kernelsquare.memberapi.common.jwt.JWTAuthenticationEntryPoint;
import com.kernelsquare.memberapi.domain.auth.service.TokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final TokenProvider tokenProvider;
	private final JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JWTAccessDeniedHandler jwtAccessDeniedHandler;

	private final String[] permitAllPatterns = new String[] {
		"/api/v1/auth/check/email",
		"/api/v1/auth/check/nickname",
		"/api/v1/auth/signup",
		"/api/v1/auth/login",
		"/actuator",
		"/actuator/**",

		// 소켓 통신의 임시 화면을 사용하기 위해 관련 경로는 permitAll
		"/chat/**",
		"/kernel-square/**",
		"/topic/chat/room",
		"/app/chat/message",
		"/webjars/**",
		"/ws/**",
		"/topic/test/room",
		"/app/test/message",
	};

	private final String[] hasAnyAuthorityPatterns = new String[] {
		"/api/v1/images",
		"/api/v1/coffeechat/reservations",
		"/api/v1/alerts/**",
	};

	private final String[] hasRoleUserPatterns = new String[] {
		"/api/v1/auth/reissue",
		"/api/v1/auth/logout",
		"/api/v1/questions/answers/{answerId}",
		"/api/v1/questions/{questionId}/answers",
		"/api/v1/questions/answers/{answerId}/vote",
		"/api/v1/coffeechat/reservations/book",
		"/api/v1/coffeechat/reservations/{reservationId}",
		"/api/v1/notices/**",
		"/api/v1/questions/{questionId}/answer-bot"
	};

	private final String[] hasRoleAdminPatterns = new String[] {
		"/api/v1/techs/{techStackId}",
		"/api/v1/levels/**"
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
			.requestMatchers(HttpMethod.GET, "/api/v1/questions/{questionId}").permitAll()
			.requestMatchers(HttpMethod.GET, "/api/v1/questions").permitAll()
			.requestMatchers(HttpMethod.GET, "/api/v1/search/questions").permitAll()
			.requestMatchers(HttpMethod.GET, "/api/v1/search/techs").permitAll()
			.requestMatchers(HttpMethod.GET, "/api/v1/questions/{questiondId}/answers").permitAll()
			.requestMatchers(HttpMethod.GET, "/api/v1/levels").permitAll()
			.requestMatchers(HttpMethod.GET, "/api/v1/coffeechat/posts").permitAll()
			.requestMatchers(HttpMethod.GET, "/api/v1/coffeechat/posts/{postId}").permitAll()
			.requestMatchers(HttpMethod.GET, "/api/v1/hashtags").permitAll()
			.requestMatchers(HttpMethod.GET, "/api/v1/techs").permitAll()
			.requestMatchers(HttpMethod.GET, "/api/v1/coding-meetings").permitAll()
			.requestMatchers(HttpMethod.GET, "/api/v1/coding-meetings/{codingMeetingToken}").permitAll()
			.requestMatchers(HttpMethod.GET, "/api/v1/coding-meeting-comments/{codingMeetingToken}").permitAll()

			// 모든 권한에 대한 접근 허용
			.requestMatchers(hasAnyAuthorityPatterns).authenticated()
			.requestMatchers(HttpMethod.GET, "/api/v1/members/{memberId}").authenticated()
			.requestMatchers(HttpMethod.GET, "/api/v1/coffeechat/rooms/{roomKey}").authenticated()
			.requestMatchers(HttpMethod.POST, "/api/v1/coffeechat/request/{memberId}").authenticated()

			// ROLE_USER 권한 필요
			.requestMatchers(hasRoleUserPatterns).hasRole("USER")
			.requestMatchers(HttpMethod.DELETE, "/api/v1/members/{memberId}").hasRole("USER")
			.requestMatchers(HttpMethod.PUT, "/api/v1/members/{memberId}/profile").hasRole("USER")
			.requestMatchers(HttpMethod.PUT, "/api/v1/members/{memberId}/password").hasRole("USER")
			.requestMatchers(HttpMethod.PUT, "/api/v1/members/{memberId}/introduction").hasRole("USER")
			.requestMatchers(HttpMethod.POST, "/api/v1/questions/**").hasRole("USER")
			.requestMatchers(HttpMethod.PUT, "/api/v1/questions/{questionId}").hasRole("USER")
			.requestMatchers(HttpMethod.DELETE, "/api/v1/questions/{questionId}").hasRole("USER")
			.requestMatchers(HttpMethod.POST, "/api/v1/questions/{questionId}/answers").hasRole("USER")
			.requestMatchers(HttpMethod.POST, "/api/v1/coding-meetings").hasRole("USER")
			.requestMatchers(HttpMethod.PUT, "/api/v1/coding-meetings/**").hasRole("USER")
			.requestMatchers(HttpMethod.DELETE, "/api/v1/coding-meetings/{codingMeetingToken}").hasRole("USER")
			.requestMatchers(HttpMethod.POST, "/api/v1/coding-meeting-comments").hasRole("USER")
			.requestMatchers(HttpMethod.PUT, "/api/v1/coding-meeting-comments/{codingMeetingCommentToken}").hasRole("USER")
			.requestMatchers(HttpMethod.DELETE, "/api/v1/coding-meeting-comments/{codingMeetingCommentToken}").hasRole("USER")

			// ROLE_MENTOR 권한 필요
			.requestMatchers(HttpMethod.POST, "/api/v1/coffeechat/posts").hasRole("MENTOR")
			.requestMatchers(HttpMethod.POST, "/api/v1/coffeechat/rooms").hasRole("MENTOR")
			.requestMatchers(HttpMethod.POST, "/api/v1/coffeechat/rooms/enter").hasAnyRole("MENTOR", "USER")
			.requestMatchers(HttpMethod.PUT, "/api/v1/coffeechat/posts/{postId}").hasRole("MENTOR")
			.requestMatchers(HttpMethod.DELETE, "/api/v1/coffeechat/posts/{postId}").hasRole("MENTOR")
			.requestMatchers(HttpMethod.POST, "/api/v1/coffeechat/rooms/{roomKey}").hasAnyRole("MENTOR", "USER")

			// ROLE_ADMIN 권한 필요
			.requestMatchers(hasRoleAdminPatterns).hasRole("ADMIN")
			.requestMatchers(HttpMethod.POST, "/api/v1/levels").hasRole("ADMIN")
			.requestMatchers(HttpMethod.POST, "/api/v1/techs").hasRole("ADMIN")
			.requestMatchers(HttpMethod.DELETE, "/api/v1/hashtags/{hashtagId}").hasRole("ADMIN")
		);

		http.addFilterBefore(new JWTSettingFilter(tokenProvider), BasicAuthenticationFilter.class);

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
