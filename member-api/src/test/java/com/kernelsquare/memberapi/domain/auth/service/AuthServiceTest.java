package com.kernelsquare.memberapi.domain.auth.service;

import com.kernelsquare.core.common_response.error.code.AuthErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.type.AuthorityType;
import com.kernelsquare.core.util.ImageUtils;
import com.kernelsquare.domainmysql.domain.auth.command.AuthCommand;
import com.kernelsquare.domainmysql.domain.auth.info.AuthInfo;
import com.kernelsquare.domainmysql.domain.authority.entity.Authority;
import com.kernelsquare.domainmysql.domain.authority.repository.AuthorityRepository;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.level.repository.LevelRepository;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.info.MemberInfo;
import com.kernelsquare.domainmysql.domain.member.repository.MemberReader;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;
import com.kernelsquare.domainmysql.domain.member_authority.repository.MemberAuthorityRepository;
import com.kernelsquare.memberapi.domain.auth.dto.SignUpRequest;
import com.kernelsquare.memberapi.domain.auth.dto.SignUpResponse;
import com.kernelsquare.memberapi.domain.auth.validation.AuthValidation;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

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
	@Mock
	private MemberReader memberReader;
	@Mock
	private AuthValidation authValidation;
	@Mock
	private TokenProvider tokenProvider;

	@Spy
	private PasswordEncoder passwordEncoder = Mockito.spy(BCryptPasswordEncoder.class);

	@Test
	@DisplayName("로그인 테스트")
	void testLogIn() throws Exception {
		//given
		String testEmail = "inthemeantime@name.com";
		String testPassword = "letmego";

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
			.authorities(List.of(
				MemberAuthority.builder()
					.member(Member.builder().build())
					.authority(Authority.builder().authorityType(AuthorityType.ROLE_USER).build())
					.build()))
			.build();

		Optional<Level> optionalLevel = Optional.of(level);

		doReturn(optionalLevel)
			.when(levelRepository)
			.findByName(anyLong());

		AuthCommand.LoginMember command = AuthCommand.LoginMember.builder()
			.email(testEmail)
			.password(testPassword)
			.build();

		doNothing()
			.when(authValidation)
			.validatePassword(anyString(), anyString());

		doReturn(member)
			.when(memberReader)
			.findMember(anyString());

		List<String> roles = member.getAuthorities().stream()
			.map(MemberAuthority::getAuthority)
			.map(Authority::getAuthorityType)
			.map(AuthorityType::getDescription)
			.toList();

		String accessToken = "dawdawdawd";
		String refreshToken = "ghsefaefaseg";

		AuthInfo.LoginInfo loginInfo = AuthInfo.LoginInfo.of(MemberInfo.from(member), roles, accessToken, refreshToken);

		doReturn(loginInfo)
			.when(tokenProvider)
			.createToken(any(MemberInfo.class));

		//when
		AuthInfo.LoginInfo response = authService.login(command);

		//then
		assertThat(response.memberId()).isEqualTo(member.getId());
		assertThat(response.level()).isEqualTo(member.getLevel().getName());
		assertThat(response.imageUrl()).isEqualTo(ImageUtils.makeImageUrl(member.getImageUrl()));
		assertThat(response.introduction()).isEqualTo(member.getIntroduction());
		assertThat(response.experience()).isNotEqualTo(member.getExperience());
		assertThat(response.nickname()).isEqualTo(member.getNickname());
		assertThat(response.roles()).isEqualTo(roles);
		assertThat(response.accessToken()).isEqualTo(accessToken);
		assertThat(response.refreshToken()).isEqualTo(refreshToken);

		//verify
		verify(memberReader, only()).findMember(anyString());
		verify(memberReader, times(1)).findMember(anyString());
		verify(authValidation, only()).validatePassword(anyString(), anyString());
		verify(authValidation, times(1)).validatePassword(anyString(), anyString());
		verify(tokenProvider, only()).createToken(any(MemberInfo.class));
		verify(tokenProvider, times(1)).createToken(any(MemberInfo.class));
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
