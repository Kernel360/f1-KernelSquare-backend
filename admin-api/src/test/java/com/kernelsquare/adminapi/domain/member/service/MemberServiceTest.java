package com.kernelsquare.adminapi.domain.member.service;

import com.kernelsquare.adminapi.domain.member.dto.FindMemberResponse;
import com.kernelsquare.core.common_response.error.code.MemberErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberReader;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("회원 서비스 통합 테스트")
@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
	@InjectMocks
	private MemberService memberService;
	@Mock
	private MemberRepository memberRepository;
	@Mock
	private MemberReader memberReader;

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
		FindMemberResponse findMemberResponse = memberService.findMember(testMemberId);

		//then
		assertThat(findMemberResponse.nickname()).isEqualTo(member.getNickname());
		assertThat(findMemberResponse.introduction()).isEqualTo(member.getIntroduction());
		assertThat(findMemberResponse.experience()).isEqualTo(member.getExperience());
		assertThat(findMemberResponse.imageUrl().length()).isGreaterThan(member.getImageUrl().length());
		assertThat(findMemberResponse.imageUrl()).endsWith(member.getImageUrl());
		assertThat(findMemberResponse.memberId()).isEqualTo(testMemberId);

		//verify
		verify(memberReader, times(1)).findMember(anyLong());
	}

	@Test
	@DisplayName("회원 탈퇴 테스트")
	void testDeleteMember() throws Exception {
		//given
		Long testMemberId = 1L;

		doNothing()
			.when(memberRepository)
			.deleteById(anyLong());

		doThrow(new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND))
			.when(memberRepository)
			.findById(anyLong());

		//when
		memberService.deleteMember(testMemberId);

		//then
		assertThatThrownBy(() -> memberRepository.findById(anyLong()))
			.isExactlyInstanceOf(BusinessException.class);

		//verify
		verify(memberRepository, times(1)).deleteById(anyLong());
		verify(memberRepository, times(1)).findById(anyLong());
	}

	private Member createTestMember() {
		return Member
			.builder()
			.id(1L)
			.nickname("hongjugwang")
			.email("jugwang@naver.com")
			.password("hashedPassword")
			.experience(10000L)
			.introduction("hi, i'm hongjugwang.")
			.imageUrl("s3:qwe12fasdawczx")
			.build();
	}
}
