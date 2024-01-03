package com.kernel360.kernelsquare.global.jwt;

import static com.kernel360.kernelsquare.global.common_response.error.code.TokenErrorCode.*;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kernel360.kernelsquare.domain.auth.dto.TokenDto;
import com.kernel360.kernelsquare.domain.auth.entity.RefreshToken;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider implements InitializingBean {
	private Key key;

	private final String AUTHORITIES_KEY = "auth";

	@Value("${spring.security.jwt.secret:not found!}")
	private String secret;

	@Value("${spring.security.jwt.access-token-validity-in-seconds}:not found!")
	private long accessTokenValidityInSeconds;

	@Value("${spring.security.jwt.refresh-token-validity-in-seconds}:not found!")
	private long refreshTokenValidityInSeconds;

	private final RedisTemplate<Long, String> redisTemplate;

	@Override
	public void afterPropertiesSet() throws Exception {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public TokenDto createToken(Authentication authentication) {
		String authorities = authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));
		return TokenDto.of(
			createAccessToken(authentication.getName(), authorities),
			createRefreshToken(authentication.getName()));
	}

	public String createAccessToken(String sub, String roles) {
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

	public String createRefreshToken(String sub) {
		LocalDateTime expirationDate = LocalDateTime.now()
			.plusSeconds(refreshTokenValidityInSeconds);

		String uuid = UUID.randomUUID().toString().replace("-", "");

		RefreshToken refreshToken = RefreshToken.builder()
			.refreshToken(uuid)
			.createdDate(LocalDateTime.now())
			.expirationDate(expirationDate)
			.memberId(Long.parseLong(sub))
			.build();

		String refreshTokenJson = toJsonString(refreshToken);
		if (redisTemplate.opsForValue().get(refreshToken.getMemberId()) != null) {
			removeRedisRefreshToken(refreshToken.getMemberId());
		}
		redisTemplate.opsForValue().set(refreshToken.getMemberId(), refreshTokenJson);

		return uuid;
	}

	private String toJsonString(RefreshToken refreshToken) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			String refreshTokenJson = mapper.writeValueAsString(refreshToken);
			return refreshTokenJson;
		} catch (JsonProcessingException exception) {
			exception.printStackTrace();
			//todo : 예외 처리
			throw new IllegalArgumentException("JsonProcessingException 발생");
		}
	}

	public RefreshToken toRefreshToken(String refreshTokenToString) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			return mapper.readValue(refreshTokenToString, RefreshToken.class);
		} catch (JsonProcessingException exception) {
			exception.printStackTrace();
			//todo : 예외 처리
			throw new IllegalArgumentException("JsonProcessingException 발생");
		}
	}

	/** 인증 정보 조회 **/
	public Authentication getAuthentication(String token) {
		Claims claims = parseClaims(token);

		List<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
			.map(SimpleGrantedAuthority::new)
			.toList();

		User principal = new User(claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}

	public Claims parseClaims(String token) {
		try {
			Claims claims = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
			return claims;
		} catch (ExpiredJwtException ex) {
			return ex.getClaims();
		}
	}

	/** Access Token 유효성 검증을 수행 **/
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

	/** Refresh Token 재발급 - RTR 적용**/
	public TokenDto reissueToken(TokenDto tokenDto) {
		Claims claims = parseClaims(tokenDto.accessToken());
		String findIdByAccessToken = parseClaims(tokenDto.accessToken()).getSubject();
		String refreshTokenToString = redisTemplate.opsForValue().get(Long.parseLong(findIdByAccessToken));

		RefreshToken refreshToken = toRefreshToken(refreshTokenToString);
		validateReissueToken(refreshToken, findIdByAccessToken);

		return TokenDto.builder()
			.accessToken(createAccessToken(String.valueOf(refreshToken.getMemberId()), claims.get("auth").toString()))
			.refreshToken(createRefreshToken(String.valueOf(refreshToken.getMemberId())))
			.build();
	}

	/** Refresh Token 유효성 검증을 수행 **/
	private void validateReissueToken(RefreshToken refreshToken, String accessTokenId) {
		if (!refreshToken.getExpirationDate().isAfter(LocalDateTime.now())) {
			removeRedisRefreshToken(refreshToken.getMemberId());
			throw new BusinessException(EXPIRED_LOGIN_INFO);
		}
		if (!accessTokenId.equals(refreshToken.getMemberId()) || !accessTokenId.equals(refreshToken.getMemberId())) {
			throw new BusinessException(INVALID_TOKEN);
		}
	}

	public void removeRedisRefreshToken(Long id) {
		redisTemplate.opsForValue().getOperations().delete(id);
	}
}

