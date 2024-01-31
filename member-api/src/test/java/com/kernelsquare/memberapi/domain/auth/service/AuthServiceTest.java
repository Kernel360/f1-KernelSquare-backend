package com.kernelsquare.memberapi.domain.auth.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.kernelsquare.memberapi.domain.auth.dto.LoginRequest;
import com.kernelsquare.memberapi.domain.auth.dto.SignUpRequest;
import com.kernelsquare.memberapi.domain.auth.dto.SignUpResponse;
import com.kernelsquare.core.common_response.error.code.AuthErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.type.AuthorityType;
import com.kernelsquare.domainmysql.domain.authority.entity.Authority;
import com.kernelsquare.domainmysql.domain.authority.repository.AuthorityRepository;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.level.repository.LevelRepository;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;
import com.kernelsquare.domainmysql.domain.member_authority.repository.MemberAuthorityRepository;

@DisplayName("인증 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
	@InjectMocks
	private AuthService authService;
	@Mock
	private MemberRepository memberRepository;
	@Mock
	private LevelRepository levelRepository;
	@Mock
	private AuthorityRepository authorityRepository;
	@Mock
	private MemberAuthorityRepository memberAuthorityRepository;

	@Spy
	private PasswordEncoder passwordEncoder = Mockito.spy(BCryptPasswordEncoder.class);

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

		Level level = Level.builder()
			.id(1L)
			.name(1L)
			.imageUrl("s3:whatever")
			.levelUpperLimit(500L)
			.build();

		Member member = Member.builder()
			.id(1L)
			.email(testEmail)
			.introduction("idontwannnaknow")
			.nickname("notnow")
			.password(passwordEncoder.encode(testPassword))
			.experience(1000L)
			.level(level)
			.imageUrl("s3:myface")
			.build();

		Optional<Member> optionalMember = Optional.of(member);
		Optional<Level> optionalLevel = Optional.of(level);

		doReturn(optionalMember)
			.when(memberRepository)
			.findByEmail(anyString());

		doReturn(optionalLevel)
			.when(levelRepository)
			.findByName(anyLong());

		//when
		Member loginMember = authService.login(loginRequest);

		//then
		assertThat(loginMember.getEmail()).isEqualTo(testEmail);
		assertThat(passwordEncoder.matches(loginRequest.password(), loginMember.getPassword())).isTrue();

		//verify
		verify(memberRepository, only()).findByEmail(anyString());
		verify(memberRepository, times(1)).findByEmail(anyString());
		verify(passwordEncoder, times(2)).matches(anyString(), anyString());
	}

	@Test
	@DisplayName("회원 가입 테스트")
	void testSignUp() throws Exception {
		//given
		String password = "hashed_password";
		String encodedPassword = passwordEncoder.encode(password);

		SignUpRequest signUpRequest = SignUpRequest.builder()
			.nickname("순두부")
			.email("whattobe@naver.com")
			.password("hashed_password")
			.build();

		Level level = Level.builder()
			.id(1L)
			.name(1L)
			.imageUrl("s3:whatever")
			.levelUpperLimit(500L)
			.build();

		Optional<Level> optionalLevel = Optional.of(level);

		Member member = Member.builder()
			.id(1L)
			.email(signUpRequest.email())
			.nickname(signUpRequest.nickname())
			.password(encodedPassword)
			.level(level)
			.build();

		Authority authority = Authority.builder()
			.id(1L)
			.authorityType(AuthorityType.ROLE_USER)
			.build();

		Optional<Authority> optionalAuthority = Optional.of(authority);

		MemberAuthority memberAuthority = MemberAuthority.builder()
			.member(member)
			.authority(authority)
			.build();

		doReturn(optionalLevel).
			when(levelRepository)
			.findByName(anyLong());

		doReturn(member)
			.when(memberRepository)
			.save(any(Member.class));

		doReturn(optionalAuthority)
			.when(authorityRepository)
			.findAuthorityByAuthorityType(any(AuthorityType.class));

		doReturn(memberAuthority)
			.when(memberAuthorityRepository)
			.save(any(MemberAuthority.class));

		//when
		SignUpResponse signUpResponse = authService.signUp(signUpRequest);

		//then
		assertThat(signUpResponse.memberId()).isNotNull();

		//verify
		verify(levelRepository, only()).findByName(anyLong());
		verify(levelRepository, times(1)).findByName(anyLong());

		verify(passwordEncoder, times(2)).encode(anyString());

		verify(memberRepository, times(1)).save(any(Member.class));
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
		verify(memberRepository, times(1)).existsByEmail(anyString());
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
		verify(memberRepository, times(1)).existsByNickname(anyString());
	}
}
