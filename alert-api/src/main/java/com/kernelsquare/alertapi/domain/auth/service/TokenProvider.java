package com.kernelsquare.alertapi.domain.auth.service;

import com.kernelsquare.alertapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.List;

import static com.kernelsquare.core.common_response.error.code.TokenErrorCode.*;

@Component
@RequiredArgsConstructor
public class TokenProvider implements InitializingBean {
	private Key key;

	private final String AUTHORITIES_KEY = "auth";

	@Value("${spring.security.jwt.secret}")
	private String secret;

	private final MemberDetailService memberDetailService;

	@Override
	public void afterPropertiesSet() throws Exception {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	/**
	 * 인증 정보 조회
	 **/
	public Authentication getAuthentication(String token) {
		Claims claims = parseClaims(token);

		List<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
			.map(SimpleGrantedAuthority::new)
			.toList();

		MemberAdapter memberAdapter = (MemberAdapter)memberDetailService.loadUserByUsername(claims.getSubject());

		return new UsernamePasswordAuthenticationToken(memberAdapter, token, authorities);
	}

	private Claims parseClaims(String token) {
		try {
			Claims claims = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
			return claims;
		} catch (ExpiredJwtException ex) {
			throw new BusinessException(EXPIRED_TOKEN);
		}
	}

	/**
	 * Access Token 유효성 검증을 수행
	 **/
	public boolean validateAccessToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException e) {
			throw new BusinessException(INVALID_TOKEN);
		} catch (ExpiredJwtException e) {
			throw new BusinessException(EXPIRED_TOKEN);
		} catch (UnsupportedJwtException e) {
			throw new BusinessException(UNAUTHORIZED_TOKEN);
		} catch (IllegalArgumentException e) {
			throw new BusinessException(WRONG_TOKEN);
		}
	}
}

