package com.kernelsquare.memberapi.domain.auth.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kernelsquare.memberapi.domain.auth.dto.LoginRequest;
import com.kernelsquare.memberapi.domain.auth.dto.TokenRequest;
import com.kernelsquare.memberapi.domain.auth.dto.TokenResponse;
import com.kernelsquare.memberapi.domain.auth.entity.RefreshToken;
import com.kernelsquare.domainmysql.domain.member.entity.Member;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@DisplayName("토큰 생성 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class TokenProviderTest {
	@InjectMocks
	private TokenProvider tokenProvider;
	@Spy
	private RedisTemplate<Long, RefreshToken> redisTemplate = spy(RedisTemplate.class);

	@Mock
	private AuthenticationManagerBuilder authenticationManagerBuilder;

	private String secret = "dGVzdF9zZWNyZXRfdGVzdF9zZWNyZXRfdGVzdF9zZWNyZXRfdGVzdF9zZWNyZXRfdGVzdF9zZWNyZXRfdGVzdF9zZWNyZXRfdGVzdF9zZWNyZXRfdGVzdF9zZWNyZXRfdGVzdF9zZWNyZXRfdGVzdF9zZWNyZXRf";

	private ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	public void setUp() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		ReflectionTestUtils.setField(tokenProvider, "key", Keys.hmacShaKeyFor(keyBytes));

		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		String classPropertyTypeName = "RefreshToken.class";
		GenericJackson2JsonRedisSerializer jsonRedisSerializer =
			new GenericJackson2JsonRedisSerializer(classPropertyTypeName);
		jsonRedisSerializer.configure(objectMapper -> objectMapper
			.registerModule(new JavaTimeModule()));

		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
		lettuceConnectionFactory.afterPropertiesSet();

		redisTemplate.setConnectionFactory(lettuceConnectionFactory);
		redisTemplate.setKeySerializer(new JdkSerializationRedisSerializer());
		redisTemplate.setValueSerializer(jsonRedisSerializer);
		redisTemplate.afterPropertiesSet();
	}

	@Test
	@DisplayName("jwt 생성 테스트")
	void testCreateToken() throws Exception {
		//given
		String email = "jugwang@naver.com";
		String password = "hashed_password";

		Member member = Member
			.builder()
			.id(1L)
			.nickname("hongjugwang")
			.email(email)
			.password(password)
			.experience(10000L)
			.introduction("hi, i'm hongjugwang.")
			.imageUrl("s3:qwe12fasdawczx")
			.build();

		LoginRequest loginRequest = LoginRequest.builder()
			.email(email)
			.password(password)
			.build();

		RefreshToken refreshToken = RefreshToken.builder()
			.memberId(member.getId())
			.refreshToken("awdawdawdawdawd")
			.createdDate(LocalDateTime.now())
			.expirationDate(LocalDateTime.now().plusDays(10))
			.build();

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
			new UsernamePasswordAuthenticationToken(member.getId(), password);

		Authentication authentication = mock(Authentication.class);

		AuthenticationManager authenticationManager = mock(AuthenticationManager.class);

		doReturn(authenticationManager)
			.when(authenticationManagerBuilder)
			.getObject();

		doReturn(authentication)
			.when(authenticationManager)
			.authenticate(usernamePasswordAuthenticationToken);

		doReturn(String.valueOf(member.getId()))
			.when(authentication)
			.getName();

		//when
		TokenResponse tokenResponse = tokenProvider.createToken(member, loginRequest);

		RefreshToken createdRefreshToken = objectMapper.readValue(Decoders.BASE64
			.decode(tokenResponse.refreshToken()), RefreshToken.class);

		//then
		assertThat(createdRefreshToken.getMemberId()).isEqualTo(member.getId());

		//verify
		verify(authenticationManagerBuilder, times(1)).getObject();
		verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
		verify(authentication, times(2)).getName();
	}

	@Test
	@DisplayName("로그 아웃 테스트")
	void testLogout() throws Exception {
		//given
		Long testMemberId = 1L;
		String accessToken = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIyIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTkwMDAwMDAwMH0.eHSaEoaFl9Rb7R6YxwyKiACOObHN0XjiNO7T7i1KpkiqVbgz9hQr5EPq5DuRliA_UlsBeIvfU8UHPG7xhwdcRg";
		String refreshTokenString = "eyJtZW1iZXJJZCI6MiwicmVmcmVzaFRva2VuIjoiMjYzOGUxYjQ3MmI2NDRkNTk4YzY1NGNlZWFlN2FhOTAiLCJjcmVhdGVkRGF0ZSI6IjIwMjQtMDEtMTBUMjE6MDA6MzIuNDYxOCIsImV4cGlyYXRpb25EYXRlIjoiMjAyNC0wMS0yNFQyMTowMDozMi40NjE3NiJ9";

		TokenRequest tokenRequest = TokenRequest.builder()
			.refreshToken(refreshTokenString)
			.accessToken(accessToken)
			.build();

		ValueOperations<Long, RefreshToken> longRefreshTokenValueOperations = mock(ValueOperations.class);

		RedisOperations<Long, RefreshToken> operations = mock(RedisOperations.class);

		doReturn(longRefreshTokenValueOperations)
			.when(redisTemplate)
			.opsForValue();

		doReturn(operations)
			.when(longRefreshTokenValueOperations)
			.getOperations();

		doReturn(Boolean.TRUE)
			.when(operations)
			.delete(anyLong());

		//when
		tokenProvider.logout(tokenRequest);
		Boolean delete = redisTemplate.opsForValue().getOperations().delete(testMemberId);

		//then
		assertThat(delete).isTrue();

		//verify
		verify(redisTemplate, times(2)).opsForValue();
		verify(redisTemplate.opsForValue(), times(2)).getOperations();
		verify(redisTemplate.opsForValue().getOperations(), times(2)).delete(anyLong());
	}

	/**
	 * AccessToken은 만료 시간을 들고 있어서 이 코드 대로면 시간이 지나면 항상 테스트가 터져버림..
	 * 따라서 임의로 AccessToken 만료 기한을 2030년까지로 설정하여 일단 테스트가 통과 될 수 있도록 지정 해놓음
	 */
	@Test
	@DisplayName("토큰 재발급 테스트")
	void testReissueToken() throws Exception {
		//given
		String accessToken = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIyIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTkwMDAwMDAwMH0.eHSaEoaFl9Rb7R6YxwyKiACOObHN0XjiNO7T7i1KpkiqVbgz9hQr5EPq5DuRliA_UlsBeIvfU8UHPG7xhwdcRg";

		String refreshTokenString = "eyJtZW1iZXJJZCI6MiwicmVmcmVzaFRva2VuIjoiMjYzOGUxYjQ3MmI2NDRkNTk4YzY1NGNlZWFlN2FhOTAiLCJjcmVhdGVkRGF0ZSI6IjIwMjQtMDEtMTBUMjE6MDA6MzIuNDYxOCIsImV4cGlyYXRpb25EYXRlIjoiMjEyNC0xMi0yNFQyMTowMDozMi40NjE3NiJ9";

		TokenRequest tokenRequest = TokenRequest
			.builder()
			.accessToken(accessToken)
			.refreshToken(refreshTokenString)
			.build();

		RefreshToken refreshToken = objectMapper.readValue(Decoders.BASE64
			.decode(refreshTokenString), RefreshToken.class);

		ValueOperations<Long, RefreshToken> longRefreshTokenValueOperations = mock(ValueOperations.class);

		doReturn(longRefreshTokenValueOperations)
			.when(redisTemplate)
			.opsForValue();

		doReturn(refreshToken)
			.when(longRefreshTokenValueOperations)
			.get(anyLong());

		//when
		TokenResponse tokenResponse = tokenProvider.reissueToken(tokenRequest);
		RefreshToken createdRefreshToken = objectMapper.readValue(Decoders.BASE64.decode(tokenResponse.refreshToken()),
			RefreshToken.class);

		//then
		assertThat(createdRefreshToken.getMemberId()).isEqualTo(refreshToken.getMemberId());

		//verify
		verify(redisTemplate, times(2)).opsForValue();
		verify(redisTemplate.opsForValue(), times(1)).get(anyLong());
	}
}
