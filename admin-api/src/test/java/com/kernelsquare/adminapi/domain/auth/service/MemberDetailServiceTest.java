package com.kernelsquare.adminapi.domain.auth.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.kernelsquare.core.type.AuthorityType;
import com.kernelsquare.domainmysql.domain.authority.entity.Authority;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;

@DisplayName("회원 디테일 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MemberDetailServiceTest {
	@InjectMocks
	private MemberDetailService memberDetailService;
	@Mock
	private MemberRepository memberRepository;

	@Test
	@DisplayName("회원 권한 주입 테스트")
	void testLoadByUserName() throws Exception {
		//given
		Level level = Level.builder()
			.name(1L)
			.imageUrl("s3:somewhere")
			.build();

		Authority authority = Authority.builder()
			.id(1L)
			.authorityType(AuthorityType.ROLE_USER)
			.build();

		Member member = Member.builder()
			.id(1L)
			.email("inthemeantime@name.com")
			.introduction("idontwannnaknow")
			.nickname("notnow")
			.level(level)
			.password("whysoserious")
			.experience(1000L)
			.imageUrl("s3:myface")
			.build();

		MemberAuthority memberAuthority = MemberAuthority.builder()
			.member(member)
			.authority(authority)
			.build();

		List<MemberAuthority> memberAuthorities = List.of(memberAuthority);

		member.initAuthorities(memberAuthorities);

		Optional<Member> optionalMember = Optional.of(member);

		Set<SimpleGrantedAuthority> authorities = member.getAuthorities().stream()
			.map(MemberAuthority::getAuthority)
			.map(auth -> new SimpleGrantedAuthority(auth.getAuthorityType().getDescription()))
			.collect(Collectors.toUnmodifiableSet());

		doReturn(optionalMember)
			.when(memberRepository)
			.findById(anyLong());

		//when
		UserDetails userDetails = memberDetailService.loadUserByUsername(String.valueOf(member.getId()));

		//then
		assertThat(userDetails.getUsername()).isEqualTo(String.valueOf(member.getId()));
		assertThat(userDetails.getPassword()).isEqualTo(member.getPassword());
		assertThat(userDetails.getAuthorities()).isEqualTo(authorities);

		//verify
		verify(memberRepository, only()).findById(anyLong());
		verify(memberRepository, times(1)).findById(anyLong());
	}
}