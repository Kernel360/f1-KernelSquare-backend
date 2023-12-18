// package com.kernel360.kernelsquare.global.jwt;
//
// import java.security.Key;
// import java.time.Instant;
// import java.time.LocalDateTime;
// import java.time.ZoneId;
// import java.util.Arrays;
// import java.util.Date;
// import java.util.List;
// import java.util.UUID;
// import java.util.stream.Collectors;
//
// import org.springframework.beans.factory.InitializingBean;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.stereotype.Component;
//
// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.databind.SerializationFeature;
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
// import com.kernel360.kernelsquare.domain.auth.dto.TokenDto;
//
// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.ExpiredJwtException;
// import io.jsonwebtoken.Header;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.MalformedJwtException;
// import io.jsonwebtoken.SignatureAlgorithm;
// import io.jsonwebtoken.UnsupportedJwtException;
// import io.jsonwebtoken.io.Decoders;
// import io.jsonwebtoken.security.Keys;
// import kr.kernel360.anabada.domain.auth.entity.RefreshToken;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
//
// //todo : 권한 일대다 관계로 바뀌어서 회원에 역방향 관계 추가해야함...
//
// @Slf4j
// @Component
// @RequiredArgsConstructor
// public class TokenProvider implements InitializingBean {
// 	private Key key;
//
// 	private final String AUTHORITIES_KEY = "auth";
//
// 	@Value("${spring.security.jwt.secret}")
// 	private String secret;
//
// 	@Value("${spring.security.jwt.access-token-validity-in-seconds}")
// 	private long accessTokenValidityInSeconds;
//
// 	@Value("${spring.security.jwt.refresh-token-validity-in-seconds}")
// 	private long refreshTokenValidityInSeconds;
//
// 	private final RedisTemplate<String, String> redisTemplate;
//
// 	@Override
// 	public void afterPropertiesSet() throws Exception {
// 		byte[] keyBytes = Decoders.BASE64.decode(secret);
// 		this.key = Keys.hmacShaKeyFor(keyBytes);
// 	}
//
// 	public TokenDto createToken(Authentication authentication) {
// 		String authorities = authentication.getAuthorities().stream()
// 			.map(GrantedAuthority::getAuthority)
// 			.collect(Collectors.joining(","));
// 		return TokenDto.of()
// 		builder()
// 			.accessToken(createAccessToken(authentication.getName(), authorities))
// 			.refreshToken(createRefreshToken(authentication.getName()))
// 			.build();
// 	}
//
// 	/** Access Token 발급 **/
// 	public String createAccessToken(String sub, String roles) {
// 		LocalDateTime localDateTime = LocalDateTime.now()
// 			.plusSeconds(accessTokenValidityInSeconds);
// 		Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
// 		Date expirationDate = Date.from(instant);
// 		return Jwts.builder()
// 			.setSubject(sub)
// 			.claim(AUTHORITIES_KEY, roles)
// 			.setExpiration(expirationDate)
// 			.signWith(key, SignatureAlgorithm.HS512)
// 			.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
// 			.compact();
// 	}
//
// 	/** Refresh Token 발급 **/
// 	public String createRefreshToken(String sub) {
// 		LocalDateTime expirationDate = LocalDateTime.now()
// 			.plusSeconds(refreshTokenValidityInSeconds);
//
// 		String uuid = UUID.randomUUID().toString().replace("-", "");
//
// 		RefreshToken refreshToken = RefreshToken.builder()
// 			.refreshToken(uuid)
// 			.createdDate(LocalDateTime.now())
// 			.expirationDate(expirationDate)
// 			.email(sub)
// 			.build();
//
// 		String refreshTokenJson = toJsonString(refreshToken);
// 		redisTemplate.opsForValue().set("refreshToken:" + refreshToken.getRefreshToken(), refreshTokenJson);
//
// 		return uuid;
// 	}
//
// 	private String toJsonString(RefreshToken refreshToken) {
// 		try {
// 			ObjectMapper mapper = new ObjectMapper();
// 			mapper.registerModule(new JavaTimeModule());
// 			mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
// 			String refreshTokenJson = mapper.writeValueAsString(refreshToken);
// 			return refreshTokenJson;
// 		} catch (JsonProcessingException exception) {
// 			exception.printStackTrace();
// 			throw new IllegalArgumentException("JsonProcessingException 발생");
// 		}
// 	}
//
// 	private RefreshToken toRefreshToken(String refreshTokenToString) {
// 		try {
// 			ObjectMapper mapper = new ObjectMapper();
// 			mapper.registerModule(new JavaTimeModule());
// 			mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
// 			return mapper.readValue(refreshTokenToString, RefreshToken.class);
// 		} catch (JsonProcessingException exception) {
// 			exception.printStackTrace();
// 			throw new IllegalArgumentException("JsonProcessingException 발생");
// 		}
// 	}
//
// 	/** 인증 정보 조회 **/
// 	public Authentication getAuthentication(String token) {
// 		Claims claims = parseClaims(token);
//
// 		List<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
// 			.map(SimpleGrantedAuthority::new)
// 			.toList();
//
// 		User principal = new User(claims.getSubject(), "", authorities);
//
// 		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
// 	}
//
// 	public Claims parseClaims(String token) {
// 		try {
// 			Claims claims = Jwts.parserBuilder()
// 				.setSigningKey(key)
// 				.build()
// 				.parseClaimsJws(token)
// 				.getBody();
// 			return claims;
// 		} catch (ExpiredJwtException ex) {
// 			return ex.getClaims();
// 		}
// 	}
//
// 	/** Access Token 유효성 검증을 수행 **/
// 	public boolean validateAccessToken(String token) {
// 		try {
// 			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
// 			return true;
// 		} catch (SecurityException | MalformedJwtException e) {
// 			throw new BusinessException(TokenErrorCode.INVALID_TOKEN);
// 		} catch (ExpiredJwtException e) {
// 			throw new BusinessException(TokenErrorCode.EXPIRED_TOKEN);
// 		} catch (UnsupportedJwtException e) {
// 			throw new BusinessException(TokenErrorCode.UNAUTHORIZED_TOKEN);
// 		} catch (IllegalArgumentException e) {
// 			throw new BusinessException(TokenErrorCode.WRONG_TOKEN);
// 		}
// 	}
//
// 	/** Refresh Token 재발급 **/
// 	public TokenDto reissueToken(TokenDto tokenDto) {
// 		Claims claims = parseClaims(tokenDto.getAccessToken());
// 		String findEmailByAccessToken = parseClaims(tokenDto.getAccessToken()).getSubject();
// 		String refreshTokenToString = redisTemplate.opsForValue().get("refreshToken:" + tokenDto.getRefreshToken());
//
// 		RefreshToken refreshToken = toRefreshToken(refreshTokenToString);
// 		validateReissueToken(refreshToken, findEmailByAccessToken);
//
// 		return TokenDto.builder()
// 			.accessToken(createAccessToken(refreshToken.getEmail(), claims.get("auth").toString()))
// 			.refreshToken(tokenDto.getRefreshToken()) // todo : 리플래시토큰 재발급할지 고민
// 			.build();
// 	}
//
// 	/** Refresh Token 유효성 검증을 수행 **/
// 	private void validateReissueToken(RefreshToken refreshToken, String accessTokenEmail) {
// 		validateRefreshTokenExpirationDate(refreshToken.getExpirationDate(), refreshToken.getRefreshToken());
// 		validateAccessTokenEmailByRefreshTokenEmail(accessTokenEmail, refreshToken.getEmail());
// 	}
//
// 	/** Refresh Token 만료기간 검증 **/
// 	private void validateRefreshTokenExpirationDate(LocalDateTime expirationDate, String refreshToken) {
// 		if (!expirationDate.isAfter(LocalDateTime.now())) {
// 			removeRedisRefreshToken(refreshToken);
// 			throw new BusinessException(TokenErrorCode.EXPIRED_LOGIN_INFO);
// 		}
// 	}
//
// 	public void removeRedisRefreshToken(String refreshToken) {
// 		redisTemplate.opsForValue().getOperations().delete("refreshToken:" + refreshToken);
// 	}
//
// 	/** Refresh Token의 Access Token이 맞는지 검증 **/
// 	private void validateAccessTokenEmailByRefreshTokenEmail(String accessTokenEmail, String refreshTokenEmail) {
// 		if (!accessTokenEmail.equals(refreshTokenEmail)) {
// 			throw new BusinessException(TokenErrorCode.INVALID_TOKEN);
// 		}
// 	}
// }
//
