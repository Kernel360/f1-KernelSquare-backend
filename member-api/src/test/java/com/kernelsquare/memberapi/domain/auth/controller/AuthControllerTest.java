package com.kernelsquare.memberapi.domain.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.kernelsquare.core.type.AuthorityType;
import com.kernelsquare.core.util.ImageUtils;
import com.kernelsquare.domainmysql.domain.authority.entity.Authority;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;
import com.kernelsquare.memberapi.config.RestDocsControllerTest;
import com.kernelsquare.memberapi.domain.auth.dto.*;
import com.kernelsquare.memberapi.domain.auth.facade.AuthFacade;
import com.kernelsquare.memberapi.domain.auth.service.AuthService;
import com.kernelsquare.memberapi.domain.auth.service.TokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.kernelsquare.core.common_response.response.code.AuthResponseCode.*;
import static com.kernelsquare.memberapi.config.ApiDocumentUtils.getDocumentRequest;
import static com.kernelsquare.memberapi.config.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("인증 컨트롤러 테스트")
@WebMvcTest(AuthController.class)
class AuthControllerTest extends RestDocsControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthService authService;

	@MockBean
	private TokenProvider tokenProvider;

	@MockBean
	private AuthFacade authFacade;

	private ObjectMapper objectMapper = new ObjectMapper().setPropertyNamingStrategy(
		PropertyNamingStrategies.SnakeCaseStrategy.INSTANCE);

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

		AuthDto.LoginRequest loginRequest = AuthDto.LoginRequest.builder()
			.email("jugwang@naver.com")
			.password("hashedPassword")
			.build();

		TokenResponse tokenResponse = TokenResponse.builder()
			.accessToken("dawdawdawd")
			.refreshToken("ghsefaefaseg")
			.build();

		AuthDto.LoginResponse response = AuthDto.LoginResponse.builder()
			.memberId(member.getId())
			.nickname(member.getNickname())
			.experience(member.getExperience())
			.introduction(member.getIntroduction())
			.imageUrl(ImageUtils.makeImageUrl(member.getImageUrl()))
			.level(member.getLevel().getName())
			.roles(member.getAuthorities().stream()
				.map(MemberAuthority::getAuthority)
				.map(Authority::getAuthorityType)
				.map(AuthorityType::getDescription)
				.toList())
			.tokenDto(tokenResponse)
			.build();

		doReturn(response)
			.when(authFacade)
			.login(any(AuthDto.LoginRequest.class));

		String jsonRequest = objectMapper.writeValueAsString(loginRequest);

		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/auth/login")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8")
			.content(jsonRequest));

		//then
		resultActions
			.andExpect(status().is(LOGIN_SUCCESS.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("login-success",
				getDocumentRequest(),
				getDocumentResponse(),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
					fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
				),
				responseFields(
					fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("커스텀 응답 코드"),
					fieldWithPath("data.member_id").type(JsonFieldType.NUMBER).description("회원 아이디"),
					fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
					fieldWithPath("data.experience").type(JsonFieldType.NUMBER)
						.description("경험치"),
					fieldWithPath("data.introduction").type(JsonFieldType.STRING)
						.description("소개글"),
					fieldWithPath("data.image_url").type(JsonFieldType.STRING)
						.description("이미지 주소"),
					fieldWithPath("data.level").type(JsonFieldType.NUMBER).description("레벨"),
					fieldWithPath("data.roles").type(JsonFieldType.ARRAY)
						.description("권한"),
					fieldWithPath("data.token_dto").type(JsonFieldType.OBJECT)
						.description("토큰 응답"),
					fieldWithPath("data.token_dto.access_token").type(JsonFieldType.STRING).description(
						"액세스 토큰"),
					fieldWithPath("data.token_dto.refresh_token").type(JsonFieldType.STRING)
						.description("리프레시 토큰"))));
		//verify
		verify(authFacade, times(1)).login(any(AuthDto.LoginRequest.class));
		verify(authFacade, only()).login(any(AuthDto.LoginRequest.class));
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

		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/auth/signup")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8")
			.content(jsonRequest));

		//then
		resultActions
			.andExpect(status().is(SIGN_UP_SUCCESS.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("sign-up-success", getDocumentRequest(), getDocumentResponse()
				, requestFields(fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
					fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
					fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"))
				, responseFields(
					fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("커스텀 응답 코드"),
					fieldWithPath("data.member_id").type(JsonFieldType.NUMBER).description("회원 아이디")
				)
			));

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

		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/auth/reissue")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8")
			.content(jsonRequest));

		//then
		resultActions
			.andExpect(status().is(ACCESS_TOKEN_REISSUED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("access-token-reissued", getDocumentRequest(), getDocumentResponse(),
				requestFields(fieldWithPath("access_token").type(JsonFieldType.STRING).description("액세스 토큰"),
					fieldWithPath("refresh_token").type(JsonFieldType.STRING).description("리프레시 토큰")),
				responseFields(
					fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("커스텀 응답 코드"),
					fieldWithPath("data.access_token").type(JsonFieldType.STRING).description("액세스 토큰"),
					fieldWithPath("data.refresh_token").type(JsonFieldType.STRING).description("리프레시 토큰"))));

		//verify
		verify(tokenProvider, times(1)).reissueToken(any(TokenRequest.class));
	}

	@Test
	@WithMockUser(roles = "{}")
	@DisplayName("이메일 중복이 아닐 경우, 200 OK와 정상 응답을 보낸다.")
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

		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/auth/check/email")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8")
			.content(jsonRequest));

		//then
		resultActions
			.andExpect(status().is(EMAIL_UNIQUE_VALIDATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("email-unique-validated", getDocumentRequest(), getDocumentResponse(),
				requestFields(fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")),
				responseFields(fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("커스텀 응답 코드"))));

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

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/api/v1/auth/check/nickname")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest));

		//then
		resultActions
			.andExpect(status().is(NICKNAME_UNIQUE_VALIDATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("nickname-unique-validated", getDocumentRequest(), getDocumentResponse(),
				requestFields(fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임")),
				responseFields(fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("커스텀 응답 코드"))));

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

		doNothing()
			.when(tokenProvider)
			.logout(any(TokenRequest.class));

		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/auth/logout")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8")
			.content(jsonRequest));

		//then
		resultActions
			.andExpect(status().is(LOGOUT_SUCCESS.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("logout-success", getDocumentRequest(), getDocumentResponse(),
				requestFields(fieldWithPath("access_token").type(JsonFieldType.STRING).description("액세스 토큰"),
					fieldWithPath("refresh_token").type(JsonFieldType.STRING).description("리프레시 토큰")),
				responseFields(fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("커스텀 응답 코드"))));

		//verify
		verify(tokenProvider, times(1)).logout(any(TokenRequest.class));
		verify(tokenProvider, only()).logout(any(TokenRequest.class));
	}
}
