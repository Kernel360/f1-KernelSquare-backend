package com.kernel360.kernelsquare.domain.auth.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.kernel360.kernelsquare.domain.auth.dto.LoginRequest;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.member.repository.MemberRepository;
import com.kernel360.kernelsquare.global.common_response.error.code.AuthErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;

@DisplayName("인증 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
	@InjectMocks
	private AuthService authService;
	@Mock
	private MemberRepository memberRepository;
	@Spy
	private PasswordEncoder passwordEncoder;

	@Test
	@DisplayName("로그인 테스트")
	void testLogIn() throws Exception {
		//given
		String testEmail = "inthemeantime@name.com";
		String testPassword = "letmego";

		LoginRequest loginRequest = LoginRequest.builder()
			.email(testEmail)
			.password(testPassword)
			.build();

		Member member = Member.builder()
			.id(1L)
			.email(testEmail)
			.introduction("idontwannnaknow")
			.nickname("notnow")
			.password(passwordEncoder.encode(testPassword))
			.experience(1000L)
			.imageUrl("s3:myface")
			.build();

		Optional<Member> optionalMember = Optional.of(member);

		doReturn(optionalMember)
			.when(memberRepository)
			.findByEmail(anyString());
		
		System.out.println("passwordEncoder = " + passwordEncoder.encode(testPassword));

		//when
		Member loginMember = authService.login(loginRequest);

		//then
		assertThat(loginMember.getEmail()).isEqualTo(testEmail);

		assertThat(passwordEncoder.matches(loginRequest.password(), loginMember.getPassword())).isTrue();

		//verify
		verify(memberRepository, only()).existsByEmail(anyString());
		verify(memberRepository, atMostOnce()).existsByEmail(anyString());
	}

	@Test
	@DisplayName("이메일 중복 확인 테스트")
	void testIsEmailUnique() throws Exception {
		//given
		String email = "idont@naver.com";

		doReturn(true).when(memberRepository).existsByEmail(anyString());

		//when

		//then
		assertThatThrownBy(() ->
			authService.isEmailUnique(email))
			.isExactlyInstanceOf(BusinessException.class)
			.extracting("errorCode")
			.isEqualTo(AuthErrorCode.ALREADY_SAVED_EMAIL);

		//verify
		verify(memberRepository, only()).existsByEmail(anyString());
		verify(memberRepository, atMostOnce()).existsByEmail(anyString());
	}

	@Test
	@DisplayName("닉네임 중복 확인 테스트")
	void testIsNicknameUnique() throws Exception {
		//given
		String nickname = "hong";

		doReturn(true)
			.when(memberRepository)
			.existsByNickname(anyString());

		//when

		//then
		assertThatThrownBy(() ->
			authService.isNicknameUnique(nickname))
			.isExactlyInstanceOf(BusinessException.class)
			.extracting("errorCode")
			.isEqualTo(AuthErrorCode.ALREADY_SAVED_NICKNAME);

		//verify
		verify(memberRepository, only()).existsByNickname(anyString());
		verify(memberRepository, atMostOnce()).existsByNickname(anyString());
	}
}
