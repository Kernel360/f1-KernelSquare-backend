package com.kernelsquare.memberapi.domain.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.auth.info.AuthInfo;
import com.kernelsquare.domainmysql.domain.member.info.MemberInfo;
import com.kernelsquare.domainredis.domain.refreshtoken.entity.RefreshToken;
import com.kernelsquare.domainredis.domain.refreshtoken.repository.RefreshTokenReader;
import com.kernelsquare.domainredis.domain.refreshtoken.repository.RefreshTokenStore;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.memberapi.domain.auth.dto.TokenRequest;
import com.kernelsquare.memberapi.domain.auth.dto.TokenResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.kernelsquare.core.common_response.error.code.TokenErrorCode.*;

@Component
@RequiredArgsConstructor
public class TokenProvider implements InitializingBean {
	private Key key;

	private final String AUTHORITIES_KEY = "auth";

	@Value("${spring.security.jwt.secret}")
	private String secret;

	@Value("${spring.security.jwt.access-token-validity-in-seconds}")
	private long accessTokenValidityInSeconds;

	@Value("${spring.security.jwt.refresh-token-validity-in-seconds}")
	private long refreshTokenValidityInSeconds;

	private final MemberDetailService memberDetailService;
	private final RefreshTokenReader refreshTokenReader;
	private final RefreshTokenStore refreshTokenStore;

	@Override
	public void afterPropertiesSet() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public void logout(TokenRequest tokenRequest) {
		RefreshToken refreshToken = toRefreshToken(
			new String(Base64.getDecoder().decode(tokenRequest.refreshToken()), StandardCharsets.UTF_8));
		refreshTokenStore.delete(refreshToken);
	}

	public AuthInfo.LoginInfo createToken(MemberInfo memberInfo) {
		MemberAdapter memberAdapter = (MemberAdapter) memberDetailService.loadUserByUsername(memberInfo.getId().toString());

		List<String> authorities = memberAdapter.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.toList();

		return AuthInfo.LoginInfo.of(
			memberInfo,
			authorities,
			createAccessToken(memberAdapter.getUsername(), String.join(",", authorities)),
			createRefreshToken(memberAdapter.getUsername()));
	}

	private String createAccessToken(String sub, String roles) {
		LocalDateTime localDateTime = LocalDateTime.now()
			.plusSeconds(accessTokenValidityInSeconds);
		Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
		Date expirationDate = Date.from(instant);
		return Jwts.builder()
			.setSubject(sub)
			.claim(AUTHORITIES_KEY, roles)
			.setExpiration(expirationDate)
			.signWith(key, SignatureAlgorithm.HS512)
			.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
			.compact();
	}

	private String createRefreshToken(String sub) {
		LocalDateTime expirationDate = LocalDateTime.now()
			.plusSeconds(refreshTokenValidityInSeconds);

		String uuid = UUID.randomUUID().toString().replace("-", "");

		RefreshToken refreshToken = RefreshToken.builder()
			.refreshToken(uuid)
			.createdDate(LocalDateTime.now())
			.expirationDate(expirationDate)
			.memberId(sub)
			.build();

		refreshTokenStore.store(refreshToken);
		return Encoders.BASE64.encode(toJsonString(refreshToken).getBytes());
	}

	private String toJsonString(RefreshToken refreshToken) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			String refreshTokenJson = mapper.writeValueAsString(refreshToken);
			return refreshTokenJson;
		} catch (JsonProcessingException exception) {
			throw new BusinessException(TOKEN_PROCESSING_ERROR);
		}
	}

	public RefreshToken toRefreshToken(String refreshTokenToString) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			return mapper.readValue(refreshTokenToString, RefreshToken.class);
		} catch (JsonProcessingException exception) {
			throw new BusinessException(TOKEN_PROCESSING_ERROR);
		}
	}

	/**
	 * 인증 정보 조회
	 **/
	public Authentication getAuthentication(String token) {
		Claims claims = parseClaims(token);

		MemberAdapter memberAdapter = (MemberAdapter) memberDetailService.loadUserByUsername(claims.getSubject());

		return new UsernamePasswordAuthenticationToken(memberAdapter, token, memberAdapter.getAuthorities());
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

	/**
	 * Refresh Token 재발급
	 **/
	public TokenResponse reissueToken(TokenRequest tokenRequest) {
		Claims claims = parseClaims(tokenRequest.accessToken());
		String findIdByAccessToken = parseClaims(tokenRequest.accessToken()).getSubject();
		RefreshToken refreshToken = refreshTokenReader.find(findIdByAccessToken);

		validateReissueToken(refreshToken, findIdByAccessToken);

		return TokenResponse.builder()
			.accessToken(createAccessToken(refreshToken.getMemberId(), claims.get("auth").toString()))
			.refreshToken(tokenRequest.refreshToken())
			.build();
	}

	/**
	 * Reissued Token 유효성 검증을 수행
	 **/
	private void validateReissueToken(RefreshToken refreshToken, String accessTokenId) {
		if (refreshToken.getExpirationDate().isBefore(LocalDateTime.now())) {
			refreshTokenStore.delete(refreshToken);
			throw new BusinessException(EXPIRED_LOGIN_INFO);
		}
		if (!accessTokenId.equals(String.valueOf(refreshToken.getMemberId()))) {
			throw new BusinessException(INVALID_TOKEN);
		}
	}
}

