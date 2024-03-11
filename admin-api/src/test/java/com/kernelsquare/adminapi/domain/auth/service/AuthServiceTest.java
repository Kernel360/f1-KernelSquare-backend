package com.kernelsquare.adminapi.domain.auth.service;

import com.kernelsquare.adminapi.domain.auth.dto.LoginRequest;
import com.kernelsquare.core.type.AuthorityType;
import com.kernelsquare.domainmysql.domain.authority.entity.Authority;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberReader;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("인증 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
	@InjectMocks
	private AuthService authService;
	@Mock
	private MemberReader memberReader;
	@Spy
	private PasswordEncoder passwordEncoder = Mockito.spy(BCryptPasswordEncoder.class);

	@Test
	@DisplayName("로그인 테스트")
	void testLogIn() throws Exception {
		//given
		String testEmail = "inthemeantime@name.com";
		String testPassword = "Letmego1!";

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

		MemberAuthority memberAuthority = MemberAuthority.builder()
			.member(member)
			.authority(Authority.builder().authorityType(AuthorityType.ROLE_ADMIN).build())
			.build();

		member.initAuthorities(List.of(memberAuthority));

		doReturn(member)
			.when(memberReader)
			.findMember(anyString());

		//when
		Member loginMember = authService.login(loginRequest);

		//then
		assertThat(loginMember.getEmail()).isEqualTo(testEmail);
		assertThat(passwordEncoder.matches(loginRequest.password(), loginMember.getPassword())).isTrue();

		//verify
		verify(memberReader, only()).findMember(anyString());
		verify(memberReader, times(1)).findMember(anyString());
		verify(passwordEncoder, times(2)).matches(anyString(), anyString());
	}

}
