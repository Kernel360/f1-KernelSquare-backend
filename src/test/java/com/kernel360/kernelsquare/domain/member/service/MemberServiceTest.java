package com.kernel360.kernelsquare.domain.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import com.kernel360.kernelsquare.domain.member.dto.UpdateMemberRequest;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.member.repository.MemberRepository;
import com.kernel360.kernelsquare.global.common_response.error.code.MemberErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;

@DisplayName("회원 서비스 통합 테스트")
@Transactional
@SpringBootTest
public class MemberServiceTest {
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberRepository memberRepository;

	Long createdMemberId;
	Member testMember;

	@BeforeEach
	void setUp() {
		Member member = createTestMember();

		testMember = memberRepository.save(member);
		createdMemberId = testMember.getId();
	}

	@Test
	@DisplayName("회원 정보 수정 테스트")
	void testUpdateMember() throws Exception {
		//given
		String newImageUrl = "s3:dagwafd4323d1";
		String newIntroduction = "bye, i'm hongjugwang.";

		//when
		memberService.updateMember(createdMemberId, new UpdateMemberRequest(
			newImageUrl, newIntroduction
		));

		Member foundMember = getFoundMember();

		//then
		assertThat(foundMember.getImageUrl()).isEqualTo(newImageUrl);
		assertThat(foundMember.getIntroduction()).isEqualTo(newIntroduction);
	}

	@Test
	@DisplayName("회원 정보 조회 테스트")
	void testFindMember() throws Exception {
		//given

		//when
		Member foundMember = getFoundMember();

		//then
		assertThat(foundMember.getNickname()).isEqualTo(testMember.getNickname());
		assertThat(foundMember.getIntroduction()).isEqualTo(testMember.getIntroduction());
	}

	@Test
	@DisplayName("회원 비밀번호 변경 테스트")
	void testUpdateMemberPassword() throws Exception {
		//given
		String newPassword = "newPassword";

		//when
		memberService.updateMemberPassword(createdMemberId, newPassword);
		Member foundMember = getFoundMember();

		//then
		assertThat(foundMember.getPassword()).isEqualTo(newPassword);
	}

	@Test
	@DisplayName("회원 탈퇴 테스트")
	void testDeleteMember() throws Exception {
		//given

		//when
		memberService.deleteMember(createdMemberId);
		BusinessException exception = assertThrows(BusinessException.class, () -> getFoundMember());

		//then
		assertThat(exception.getErrorCode()).isExactlyInstanceOf(MemberErrorCode.class);
		assertThat(exception.getErrorCode()).isEqualTo(MemberErrorCode.MEMBER_NOT_FOUND);
		assertThat(exception.getErrorCode().getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(exception.getErrorCode().getMsg()).isEqualTo("존재하지 않는 회원입니다.");
	}

	private Member getFoundMember() {
		return memberRepository
			.findById(createdMemberId)
			.orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
	}

	private Member createTestMember() {
		return Member
			.builder()
			.nickname("hongjugwang")
			.email("jugwang@naver.com")
			.password("hashedPassword")
			.experience(10000L)
			.introduction("hi, i'm hongjugwang.")
			.imageUrl("s3:qwe12fasdawczx")
			.build();
	}
}
