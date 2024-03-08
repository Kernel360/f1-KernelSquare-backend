package com.kernelsquare.adminapi.domain.auth.service;

import static com.kernelsquare.core.common_response.error.code.TokenErrorCode.*;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.kernelsquare.adminapi.domain.auth.dto.MemberAdapter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kernelsquare.adminapi.domain.auth.dto.LoginRequest;
import com.kernelsquare.adminapi.domain.auth.dto.TokenRequest;
import com.kernelsquare.adminapi.domain.auth.dto.TokenResponse;
import com.kernelsquare.adminapi.domain.auth.entity.RefreshToken;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.member.entity.Member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

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

	private final RedisTemplate<Long, RefreshToken> redisTemplate;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final MemberDetailService memberDetailService;

	@Override
	public void afterPropertiesSet() throws Exception {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public void logout(TokenRequest tokenRequest) {
		RefreshToken refreshToken = toRefreshToken(
			new String(Base64.getDecoder().decode(tokenRequest.refreshToken()), StandardCharsets.UTF_8));
		redisTemplate.opsForValue().getOperations().delete(refreshToken.getMemberId());
	}

	public TokenResponse createToken(Member member, LoginRequest loginRequest) {
		UsernamePasswordAuthenticationToken authenticationToken =
			new UsernamePasswordAuthenticationToken(member.getId(), loginRequest.password());
		Authentication authentication = authenticationManagerBuilder.getObject()
			.authenticate(authenticationToken);
		String authorities = authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));
		return TokenResponse.of(
			createAccessToken(authentication.getName(), authorities),
			createRefreshToken(authentication.getName()));
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
			.memberId(Long.parseLong(sub))
			.build();

		redisTemplate.opsForValue().set(refreshToken.getMemberId(), refreshToken);

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

	/**
	 * Refresh Token 재발급
	 **/
	public TokenResponse reissueToken(TokenRequest tokenRequest) {
		Claims claims = parseClaims(tokenRequest.accessToken());
		String findIdByAccessToken = parseClaims(tokenRequest.accessToken()).getSubject();
		RefreshToken refreshToken = redisTemplate.opsForValue().get(Long.parseLong(findIdByAccessToken));

		validateReissueToken(refreshToken, findIdByAccessToken);

		return TokenResponse.builder()
			.accessToken(createAccessToken(String.valueOf(refreshToken.getMemberId()), claims.get("auth").toString()))
			.refreshToken(createRefreshToken(String.valueOf(refreshToken.getMemberId())))
			.build();
	}

	/**
	 * Reissued Token 유효성 검증을 수행
	 **/
	private void validateReissueToken(RefreshToken refreshToken, String accessTokenId) {
		if (!refreshToken.getExpirationDate().isAfter(LocalDateTime.now())) {
			redisTemplate.opsForValue().getOperations().delete(refreshToken.getMemberId());
			throw new BusinessException(EXPIRED_LOGIN_INFO);
		}
		if (!accessTokenId.equals(String.valueOf(refreshToken.getMemberId()))) {
			throw new BusinessException(INVALID_TOKEN);
		}
	}
}

