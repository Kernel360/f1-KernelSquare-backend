package com.kernelsquare.memberapi.domain.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.kernelsquare.core.common_response.error.code.MemberErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;
import com.kernelsquare.memberapi.domain.member.dto.FindMemberResponse;
import com.kernelsquare.memberapi.domain.member.dto.UpdateMemberIntroductionRequest;
import com.kernelsquare.memberapi.domain.member.dto.UpdateMemberProfileRequest;

@DisplayName("회원 서비스 통합 테스트")
@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
	@InjectMocks
	private MemberService memberService;
	@Mock
	private MemberRepository memberRepository;
	@Mock
	private PasswordEncoder passwordEncoder;

	@Test
	@DisplayName("회원 프로필 수정 테스트")
	void testUpdateMemberProfile() throws Exception {
		//given
		Long testMemberId = 1L;

		String newImageUrl = "s3:dagwafd4323d1";

		UpdateMemberProfileRequest updateMemberProfileRequest = UpdateMemberProfileRequest
			.builder()
			.imageUrl(newImageUrl)
			.build();

		Level level = Level.builder()
			.imageUrl("level 1")
			.name(1L)
			.build();

		Member member = createTestMember();
		Optional<Member> optionalMember = Optional.of(member);
		Member updatedMember = Member.builder()
			.nickname(member.getNickname())
			.imageUrl(newImageUrl)
			.level(level)
			.email(member.getEmail())
			.authorities(member.getAuthorities())
			.password(member.getPassword())
			.build();
		Optional<Member> optionalUpdatedMember = Optional.of(updatedMember);

		doReturn(optionalMember)
			.when(memberRepository)
			.findById(anyLong());

		//when
		memberService.updateMemberProfile(testMemberId, updateMemberProfileRequest);

		doReturn(optionalUpdatedMember)
			.when(memberRepository)
			.findById(anyLong());

		Optional<Member> optionalFoundMember = memberRepository.findById(testMemberId);

		//then
		assertThat(optionalFoundMember.get().getImageUrl()).isEqualTo(newImageUrl);

		//verify
		verify(memberRepository, times(2)).findById(anyLong());
	}

	@Test
	@DisplayName("회원 소개 수정 테스트")
	void testUpdateMemberIntroduction() throws Exception {
		//given
		Long testMemberId = 1L;

		String newIntroduction = "bye, i'm hongjugwang.";

		UpdateMemberIntroductionRequest updateMemberIntroductionRequest = UpdateMemberIntroductionRequest
			.builder()
			.introduction(newIntroduction)
			.build();

		Level level = Level.builder()
			.imageUrl("level 1")
			.name(1L)
			.build();

		Member member = createTestMember();
		Optional<Member> optionalMember = Optional.of(member);
		Member updatedMember = Member.builder()
			.nickname(member.getNickname())
			.introduction(newIntroduction)
			.level(level)
			.email(member.getEmail())
			.authorities(member.getAuthorities())
			.password(member.getPassword())
			.build();
		Optional<Member> optionalUpdatedMember = Optional.of(updatedMember);

		doReturn(optionalMember)
			.when(memberRepository)
			.findById(anyLong());

		//when
		memberService.updateMemberIntroduction(testMemberId, updateMemberIntroductionRequest);

		doReturn(optionalUpdatedMember)
			.when(memberRepository)
			.findById(anyLong());

		Optional<Member> optionalFoundMember = memberRepository.findById(testMemberId);

		//then
		assertThat(optionalFoundMember.get().getIntroduction()).isEqualTo(newIntroduction);

		//verify
		verify(memberRepository, times(2)).findById(anyLong());
	}

	@Test
	@DisplayName("회원 비밀번호 변경 테스트")
	void testUpdateMemberPassword() throws Exception {
		//given
		Long testMemberId = 1L;

		String newPassword = "newPassword";

		Level level = Level.builder()
			.imageUrl("level 1")
			.name(1L)
			.build();

		Member member = createTestMember();
		Optional<Member> optionalMember = Optional.of(member);
		Member updatedMember = Member.builder()
			.nickname(member.getNickname())
			.imageUrl(member.getImageUrl())
			.introduction(member.getIntroduction())
			.level(level)
			.email(member.getEmail())
			.authorities(member.getAuthorities())
			.password(newPassword)
			.build();
		Optional<Member> optionalUpdatedMember = Optional.of(updatedMember);

		doReturn(optionalMember)
			.when(memberRepository)
			.findById(anyLong());

		doReturn(newPassword)
			.when(passwordEncoder)
			.encode(anyString());

		//when
		memberService.updateMemberPassword(testMemberId, newPassword);

		doReturn(optionalUpdatedMember)
			.when(memberRepository)
			.findById(anyLong());

		Optional<Member> optionalFoundMember = memberRepository.findById(testMemberId);

		//then
		System.out.println("newPassword = " + optionalFoundMember.get().getPassword());
		assertThat(optionalFoundMember.get().getPassword()).isEqualTo(newPassword);

		//verify
		verify(memberRepository, times(2)).findById(anyLong());
		verify(passwordEncoder, times(1)).encode(anyString());
	}

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
		Optional<Member> optionalMember = Optional.of(member);

		doReturn(optionalMember)
			.when(memberRepository)
			.findById(anyLong());

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
		verify(memberRepository, times(1)).findById(anyLong());
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
