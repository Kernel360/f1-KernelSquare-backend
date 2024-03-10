package com.kernelsquare.adminapi.domain.auth.service;

import com.kernelsquare.adminapi.domain.auth.dto.MemberAdapter;
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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("회원 디테일 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MemberDetailServiceTest {
	@InjectMocks
	private MemberDetailService memberDetailService;
	@Mock
	private MemberReader memberReader;

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
			.authorityType(AuthorityType.ROLE_ADMIN)
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

		doReturn(member)
			.when(memberReader)
			.findMember(anyLong());

		//when
		MemberAdapter memberAdapter = (MemberAdapter) memberDetailService.loadUserByUsername(String.valueOf(member.getId()));

		//then

		assertThat(memberAdapter.getMember()).isEqualTo(member);

		//verify
		verify(memberReader, only()).findMember(anyLong());
		verify(memberReader, times(1)).findMember(anyLong());
	}
}