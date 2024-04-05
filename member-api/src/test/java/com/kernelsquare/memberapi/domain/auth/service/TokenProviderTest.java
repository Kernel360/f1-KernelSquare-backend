package com.kernelsquare.memberapi.domain.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kernelsquare.core.type.AuthorityType;
import com.kernelsquare.domainmysql.domain.auth.info.AuthInfo;
import com.kernelsquare.domainmysql.domain.authority.entity.Authority;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.info.MemberInfo;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;
import com.kernelsquare.domainredis.domain.refreshtoken.entity.RefreshToken;
import com.kernelsquare.domainredis.domain.refreshtoken.repository.RefreshTokenReader;
import com.kernelsquare.domainredis.domain.refreshtoken.repository.RefreshTokenStore;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdaptorInstance;
import com.kernelsquare.memberapi.domain.auth.dto.TokenRequest;
import com.kernelsquare.memberapi.domain.auth.dto.TokenResponse;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("토큰 생성 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class TokenProviderTest {
	@InjectMocks
	private TokenProvider tokenProvider;
	@Mock
	private MemberDetailService memberDetailService;
	@Mock
	private RefreshTokenReader refreshTokenReader;
	@Mock
	private RefreshTokenStore refreshTokenStore;

	private String secret = "dGVzdF9zZWNyZXRfdGVzdF9zZWNyZXRfdGVzdF9zZWNyZXRfdGVzdF9zZWNyZXRfdGVzdF9zZWNyZXRfdGVzdF9zZWNyZXRfdGVzdF9zZWNyZXRfdGVzdF9zZWNyZXRfdGVzdF9zZWNyZXRfdGVzdF9zZWNyZXRf";

	private ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	public void setUp() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		ReflectionTestUtils.setField(tokenProvider, "key", Keys.hmacShaKeyFor(keyBytes));

		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}

	@Test
	@DisplayName("jwt 생성 테스트")
	void testCreateToken() throws Exception {
		//given
		Level level = Level.builder()
			.id(1L)
			.name(1L)
			.imageUrl("s3:whatever")
			.levelUpperLimit(500L)
			.build();

		Member member = Member.builder()
			.id(1L)
			.nickname("machine")
			.email("awdag@nsavasc.om")
			.password("hashed")
			.experience(1200L)
			.introduction("basfas")
			.authorities(List.of(
				MemberAuthority.builder()
					.member(Member.builder().build())
					.authority(Authority.builder().authorityType(AuthorityType.ROLE_USER).build())
					.build()))
			.imageUrl("agawsc")
			.level(level)
			.build();

		MemberAdapter memberAdapter = new MemberAdapter(MemberAdaptorInstance.of(member));

		doReturn(memberAdapter)
			.when(memberDetailService)
			.loadUserByUsername(anyString());

		//when
		AuthInfo.LoginInfo loginInfo = tokenProvider.createToken(MemberInfo.from(member));

		RefreshToken createdRefreshToken = objectMapper.readValue(Decoders.BASE64
			.decode(loginInfo.refreshToken()), RefreshToken.class);

		//then
		assertThat(createdRefreshToken.getMemberId()).isEqualTo(member.getId().toString());

		//verify
		verify(memberDetailService, only()).loadUserByUsername(anyString());
		verify(memberDetailService, times(1)).loadUserByUsername(anyString());
	}

	@Test
	@DisplayName("로그 아웃 테스트")
	void testLogout() throws Exception {
		//given
		String accessToken = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIyIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTkwMDAwMDAwMH0.eHSaEoaFl9Rb7R6YxwyKiACOObHN0XjiNO7T7i1KpkiqVbgz9hQr5EPq5DuRliA_UlsBeIvfU8UHPG7xhwdcRg";
		String refreshTokenString = "eyJtZW1iZXJJZCI6MiwicmVmcmVzaFRva2VuIjoiMjYzOGUxYjQ3MmI2NDRkNTk4YzY1NGNlZWFlN2FhOTAiLCJjcmVhdGVkRGF0ZSI6IjIwMjQtMDEtMTBUMjE6MDA6MzIuNDYxOCIsImV4cGlyYXRpb25EYXRlIjoiMjAyNC0wMS0yNFQyMTowMDozMi40NjE3NiJ9";

		TokenRequest tokenRequest = TokenRequest.builder()
			.refreshToken(refreshTokenString)
			.accessToken(accessToken)
			.build();

		doNothing()
			.when(refreshTokenStore)
				.delete(any(RefreshToken.class));

		//when
		tokenProvider.logout(tokenRequest);

		//verify
		verify(refreshTokenStore, times(1)).delete(any(RefreshToken.class));
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

		System.out.println("refreshToken를 함 까보자 : " + refreshToken.getRefreshToken());

		doReturn(refreshToken)
			.when(refreshTokenReader)
			.find(anyString());

		//when
		TokenResponse tokenResponse = tokenProvider.reissueToken(tokenRequest);

		//then
		assertThat(tokenResponse.accessToken()).isNotEqualTo(accessToken);
		assertThat(tokenResponse.refreshToken()).isEqualTo(refreshTokenString);

		//verify
		verify(refreshTokenReader, times(1)).find(anyString());
	}
}
