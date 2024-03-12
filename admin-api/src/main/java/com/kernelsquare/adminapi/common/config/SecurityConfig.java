package com.kernelsquare.adminapi.common.config;

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

import com.kernelsquare.adminapi.common.filter.JWTSettingFilter;
import com.kernelsquare.adminapi.common.jwt.JWTAccessDeniedHandler;
import com.kernelsquare.adminapi.common.jwt.JWTAuthenticationEntryPoint;
import com.kernelsquare.adminapi.domain.auth.service.TokenProvider;

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
		"/api/v1/auth/login",
		"/actuator",
		"/actuator/**",
	};

	private final String[] hasAnyAuthorityPatterns = new String[] {
		"/api/v1/images"
	};

	private final String[] hasRoleUserPatterns = new String[] {
		"/api/v1/auth/reissue",
		"/api/v1/auth/logout",
		"/api/v1/questions/answers/{answerId}",
		"/api/v1/questions/{questionId}/answers",
		"/api/v1/questions/answers/{answerId}/vote",
	};

	private final String[] hasRoleAdminPatterns = new String[] {
		"/api/v1/techs/{techStackId}",
		"/api/v1/levels/**",
		"/api/v1/notices/**",
		"/api/v1/members/**"
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
			.requestMatchers(HttpMethod.GET, "/api/v1/questions/{questiondId}/answers").permitAll()
			.requestMatchers(HttpMethod.GET, "/api/v1/levels").permitAll()
			.requestMatchers(HttpMethod.GET, "/api/v1/coffeechat/posts").permitAll()
			.requestMatchers(HttpMethod.GET, "/api/v1/coffeechat/posts/{postId}").permitAll()
			.requestMatchers(HttpMethod.GET, "/api/v1/hashtags").permitAll()

			// 모든 권한에 대한 접근 허용
			.requestMatchers(hasAnyAuthorityPatterns).authenticated()
			.requestMatchers(HttpMethod.GET, "/api/v1/techs").authenticated()

			// ROLE_USER 권한 필요
			.requestMatchers(hasRoleUserPatterns).hasRole("USER")
			.requestMatchers(HttpMethod.POST, "/api/v1/questions/**").hasRole("USER")
			.requestMatchers(HttpMethod.PUT, "/api/v1/questions/{questionId}").hasRole("USER")
			.requestMatchers(HttpMethod.DELETE, "/api/v1/questions/{questionId}").hasRole("USER")
			.requestMatchers(HttpMethod.POST, "/api/v1/questions/{questionId}/answers").hasRole("USER")

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

		http.logout(Customizer.withDefaults());

		return http.build();
	}
}
