package com.kernelsquare.adminapi.domain.member.service;

import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.info.MemberInfo;
import com.kernelsquare.domainmysql.domain.member.repository.MemberReader;
import com.kernelsquare.domainmysql.domain.member.repository.MemberStore;
import com.kernelsquare.domainmysql.domain.member.service.MemberServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("회원 서비스 통합 테스트")
@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
	@InjectMocks
	private MemberServiceImpl memberService;
	@Mock
	private MemberReader memberReader;
	@Mock
	private MemberStore memberStore;

	@Test
	@DisplayName("회원 정보 조회 테스트")
	void testFindMember() throws Exception {
		//given
		Long testMemberId = 1L;

		Level level = Level.builder()
			.id(1L)
			.name(1L)
			.imageUrl("s3:adcqw")
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
			.level(level)
			.build();

		doReturn(member)
			.when(memberReader)
			.findMember(anyLong());

		//when
		MemberInfo findMember = memberService.findMember(testMemberId);

		//then
		assertThat(findMember.getNickname()).isEqualTo(member.getNickname());
		assertThat(findMember.getIntroduction()).isEqualTo(member.getIntroduction());
		assertThat(findMember.getExperience()).isEqualTo(member.getExperience());
		assertThat(findMember.getImageUrl().length()).isGreaterThan(member.getImageUrl().length());
		assertThat(findMember.getImageUrl()).endsWith(member.getImageUrl());
		assertThat(findMember.getId()).isEqualTo(testMemberId);

		//verify
		verify(memberReader, times(1)).findMember(anyLong());
	}

	@Test
	@DisplayName("회원 탈퇴 테스트")
	void testDeleteMember() throws Exception {
		//given
		Long testMemberId = 4L;

		doNothing()
			.when(memberStore)
			.deleteMember(anyLong());

		//when
		memberService.deleteMember(testMemberId);

		//verify
		verify(memberStore, times(1)).deleteMember(anyLong());
	}
}
