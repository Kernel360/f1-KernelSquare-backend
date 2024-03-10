package com.kernelsquare.memberapi.common.filter;

import java.io.IOException;
import java.security.Key;

import com.kernelsquare.core.common_response.error.exception.BusinessException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kernelsquare.memberapi.domain.auth.service.TokenProvider;
import com.kernelsquare.core.constants.FilterHttpMethodConstants;
import com.kernelsquare.core.constants.SecurityConstants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import static com.kernelsquare.core.common_response.error.code.TokenErrorCode.*;
import static com.kernelsquare.core.common_response.error.code.TokenErrorCode.WRONG_TOKEN;

@Component
@RequiredArgsConstructor
public class JWTSettingFilter extends OncePerRequestFilter implements InitializingBean {
	private final TokenProvider tokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		HttpServletRequest httpServletRequest = request;
		String jwt = resolveToken(httpServletRequest);

		if (StringUtils.hasText(jwt) && tokenProvider.validateAccessToken(jwt)) {
			Authentication authentication = tokenProvider.getAuthentication(jwt);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(request, response);
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			return bearerToken.substring(SecurityConstants.TOKEN_PREFIX.length()).trim();
		}
		return null;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String servletPath = request.getServletPath();
		String httpMethod = request.getMethod();

		if (servletPath.contains("/api/v1/auth") && !servletPath.equals("/api/v1/auth/logout") && !servletPath.equals(
			"/api/v1/auth/reissue")) {
			return true;
		} else if (servletPath.equals("/api/v1/questions") && httpMethod.equals(FilterHttpMethodConstants.GET)) {
			return true;
		} else if (servletPath.equals("/api/v1/answers") && httpMethod.equals(FilterHttpMethodConstants.GET)) {
			return true;
		}
		return false;
	}
}
