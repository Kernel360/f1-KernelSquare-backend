package com.kernelsquare.memberapi.domain.auth.controller;

import static com.kernelsquare.core.common_response.response.code.AuthResponseCode.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kernelsquare.core.type.AuthorityType;
import com.kernelsquare.domainmysql.domain.authority.entity.Authority;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;
import com.kernelsquare.memberapi.domain.auth.dto.CheckDuplicateEmailRequest;
import com.kernelsquare.memberapi.domain.auth.dto.CheckDuplicateNicknameRequest;
import com.kernelsquare.memberapi.domain.auth.dto.LoginRequest;
import com.kernelsquare.memberapi.domain.auth.dto.SignUpRequest;
import com.kernelsquare.memberapi.domain.auth.dto.SignUpResponse;
import com.kernelsquare.memberapi.domain.auth.dto.TokenRequest;
import com.kernelsquare.memberapi.domain.auth.dto.TokenResponse;
import com.kernelsquare.memberapi.domain.auth.service.AuthService;
import com.kernelsquare.memberapi.domain.auth.service.TokenProvider;

@DisplayName("인증 컨트롤러 테스트")
@WebMvcTest(AuthController.class)
public class AuthControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthService authService;

	@MockBean
	private TokenProvider tokenProvider;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Test
	@WithMockUser(roles = "{}")
	@DisplayName("회원 로그인 성공시, 200 OK와 정상 응답을 반환한다.")
	void testLogin() throws Exception {
		//given
		Level testLevel = Level.builder()
			.id(1L)
			.name(1L)
			.imageUrl("s3:dq1234512")
			.build();

		Authority authority = Authority.builder()
			.id(1L)
			.authorityType(AuthorityType.ROLE_USER)
			.build();

		Member member = Member
			.builder()
			.id(1L)
			.nickname("hongjugwang")
			.email("jugwang@naver.com")
			.password("hashedPassword")
			.experience(10000L)
			.introduction("hi, i'm hongjugwang.")
			.imageUrl("s3:qwe12fasdawczx")
			.level(testLevel)
			.build();

		MemberAuthority memberAuthority = MemberAuthority
			.builder()
			.member(member)
			.authority(authority)
			.build();

		List<MemberAuthority> memberAuthorityList = List.of(memberAuthority);
		member.initAuthorities(memberAuthorityList);

		LoginRequest loginRequest = LoginRequest.builder()
			.email("jugwang@naver.com")
			.password("hashedPassword")
			.build();

		TokenResponse tokenResponse = TokenResponse.builder()
			.accessToken("dawdawdawd")
			.refreshToken("ghsefaefaseg")
			.build();

		doReturn(member)
			.when(authService)
			.login(any(LoginRequest.class));

		doReturn(tokenResponse)
			.when(tokenProvider)
			.createToken(any(Member.class), any(LoginRequest.class));

		String jsonRequest = objectMapper.writeValueAsString(loginRequest);

		//when & then
		mockMvc.perform(post("/api/v1/auth/login")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest))
			.andExpect(status().is(LOGIN_SUCCESS.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(LOGIN_SUCCESS.getCode()))
			.andExpect(jsonPath("$.msg").value(LOGIN_SUCCESS.getMsg()));

		//verify
		verify(authService, times(1)).login(any(LoginRequest.class));
		verify(authService, only()).login(any(LoginRequest.class));
		verify(tokenProvider, times(1)).createToken(any(Member.class), any(LoginRequest.class));
		verify(tokenProvider, only()).createToken(any(Member.class), any(LoginRequest.class));
	}

	@Test
	@WithMockUser(roles = "{}")
	@DisplayName("회원 가입에 성공하면, 200 OK와 정상 응답을 반환한다.")
	void testSignUp() throws Exception {
		//given
		SignUpRequest signUpRequest = SignUpRequest
			.builder()
			.nickname("woww")
			.email("jugwang@naver.com")
			.password("hashedPassw@1d")
			.build();

		Member member = Member
			.builder()
			.id(1L)
			.nickname("woww")
			.email("jugwang@naver.com")
			.password("hashedPassw@1")
			.experience(10000L)
			.introduction("hi, i'm hongjugwang.")
			.imageUrl("s3:qwe12fasdawczx")
			.build();

		SignUpResponse signUpResponse = SignUpResponse
			.of(member);

		String jsonRequest = objectMapper.writeValueAsString(signUpRequest);

		doReturn(signUpResponse)
			.when(authService)
			.signUp(any(SignUpRequest.class));

		//when & then
		mockMvc.perform(post("/api/v1/auth/signup")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest))
			.andExpect(status().is(SIGN_UP_SUCCESS.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(SIGN_UP_SUCCESS.getCode()))
			.andExpect(jsonPath("$.msg").value(SIGN_UP_SUCCESS.getMsg()));

		//verify
		verify(authService, times(1)).signUp(any(SignUpRequest.class));
		verify(authService, only()).signUp(any(SignUpRequest.class));
	}

	@Test
	@WithMockUser
	@DisplayName("액세스 토큰 재발급 성공 시, 200 OK와 정상 응답을 반환한다.")
	void testReissueAccessToken() throws Exception {
		//given
		TokenRequest tokenRequest = TokenRequest
			.builder()
			.accessToken("dasdas")
			.refreshToken("gawgawgawgawg")
			.build();

		TokenResponse tokenResponse = TokenResponse.builder()
			.accessToken("dawdawdawd")
			.refreshToken("ghsefaefaseg")
			.build();

		String jsonRequest = objectMapper.writeValueAsString(tokenRequest);

		doReturn(tokenResponse)
			.when(tokenProvider)
			.reissueToken(any(TokenRequest.class));

		//when & then
		mockMvc.perform(post("/api/v1/auth/reissue")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest))
			.andExpect(status().is(ACCESS_TOKEN_REISSUED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(ACCESS_TOKEN_REISSUED.getCode()))
			.andExpect(jsonPath("$.msg").value(ACCESS_TOKEN_REISSUED.getMsg()));

		//verify
		verify(tokenProvider, times(1)).reissueToken(any(TokenRequest.class));
	}

	@Test
	@WithMockUser(roles = "{}")
	@DisplayName("이메일 중복이 아니면, 200 OK와 정상 응답을 보낸다.")
	void testIsEmailUnique() throws Exception {
		//given
		CheckDuplicateEmailRequest checkDuplicateEmailRequest
			= CheckDuplicateEmailRequest
			.builder()
			.email("hongju@naver.com")
			.build();

		String jsonRequest = objectMapper.writeValueAsString(checkDuplicateEmailRequest);

		doNothing()
			.when(authService)
			.isEmailUnique(anyString());

		//when & then
		mockMvc.perform(post("/api/v1/auth/check/email")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest))
			.andExpect(status().is(EMAIL_UNIQUE_VALIDATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(EMAIL_UNIQUE_VALIDATED.getCode()))
			.andExpect(jsonPath("$.msg").value(EMAIL_UNIQUE_VALIDATED.getMsg()));

		//verify
		verify(authService, times(1)).isEmailUnique(anyString());
		verify(authService, only()).isEmailUnique(anyString());
	}

	@Test
	@WithMockUser(roles = "{}")
	@DisplayName("닉네임이 중복이 아니면, 200 OK와 정상 응답을 반환한다.")
	void testNicknameUnique() throws Exception {
		//given
		CheckDuplicateNicknameRequest checkDuplicateNicknameRequest =
			CheckDuplicateNicknameRequest
				.builder()
				.nickname("야야야야")
				.build();

		String jsonRequest = objectMapper.writeValueAsString(checkDuplicateNicknameRequest);

		doNothing()
			.when(authService)
			.isNicknameUnique(anyString());

		//when & then
		mockMvc.perform(post("/api/v1/auth/check/nickname")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest))
			.andExpect(status().is(NICKNAME_UNIQUE_VALIDATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(NICKNAME_UNIQUE_VALIDATED.getCode()))
			.andExpect(jsonPath("$.msg").value(NICKNAME_UNIQUE_VALIDATED.getMsg()));

		//verify
		verify(authService, times(1)).isNicknameUnique(anyString());
		verify(authService, only()).isNicknameUnique(anyString());
	}

	@Test
	@WithMockUser
	@DisplayName("로그아웃 성공 시, 200 OK와 정상 응답을 보낸다")
	void testLogOut() throws Exception {
		//given
		TokenRequest tokenRequest = TokenRequest
			.builder()
			.accessToken("dasdas")
			.refreshToken(
				"IntcIlJlZnJlc2hUb2tlbi5jbGFzc1wiOlwiY29tLmtlcm5lbDM2MC5rZXJuZWxzcXVhcmUuZG9tYWluLmF1dGguZW50aXR5LlJlZnJlc2hUb2tlblwiLFwibWVtYmVySWRcIjoxLFwicmVmcmVzaFRva2VuXCI6XCJkM2Y4M2FjMzAxZTQ0N2M2OTVkMzcyNDAyNDdhYzFiZFwiLFwiY3JlYXRlZERhdGVcIjpbMjAyNCwxLDExLDksNCw1OCw1NzM2OTkwMDBdLFwiZXhwaXJhdGlvbkRhdGVcIjpbMjAyNCwxLDExLDksNCw1OCw1NzM1NTMwMDBdfSI=")
			.build();

		String jsonRequest = objectMapper.writeValueAsString(tokenRequest);

		System.out.println("tokenRequest.accessToken() = " + tokenRequest.accessToken());
		System.out.println("tokenRequest.refreshToken() = " + tokenRequest.refreshToken());
		System.out.println("jsonRequest = " + jsonRequest);

		doNothing()
			.when(tokenProvider)
			.logout(any(TokenRequest.class));

		//when & then
		mockMvc.perform(post("/api/v1/auth/logout")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest))
			.andExpect(status().is(LOGOUT_SUCCESS.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(LOGOUT_SUCCESS.getCode()))
			.andExpect(jsonPath("$.msg").value(LOGOUT_SUCCESS.getMsg()));

		//verify
		verify(tokenProvider, times(1)).logout(any(TokenRequest.class));
		verify(tokenProvider, only()).logout(any(TokenRequest.class));
	}
}
