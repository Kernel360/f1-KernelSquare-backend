package com.kernelsquare.memberapi.domain.member.controller;

import static com.kernelsquare.core.common_response.error.code.MemberErrorCode.*;
import static com.kernelsquare.core.common_response.response.code.MemberResponseCode.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.memberapi.domain.member.dto.FindMemberResponse;
import com.kernelsquare.memberapi.domain.member.dto.UpdateMemberIntroductionRequest;
import com.kernelsquare.memberapi.domain.member.dto.UpdateMemberProfileRequest;
import com.kernelsquare.memberapi.domain.member.service.MemberService;

@DisplayName("회원 컨트롤러 단위 테스트")
@WebMvcTest(MemberController.class)
public class MemberControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private MemberService memberService;

	private ObjectMapper objectMapper = new ObjectMapper();

	private Level testLevel = Level.builder()
		.id(1L)
		.name(1L)
		.imageUrl("s3:dq1234512")
		.build();

	private Member testMember = Member
		.builder()
		.nickname("hongjugwang")
		.email("jugwang@naver.com")
		.password("hashedPassword")
		.experience(10000L)
		.introduction("hi, i'm hongjugwang.")
		.imageUrl("s3:qwe12fasdawczx")
		.level(testLevel)
		.build();

	private Long testMemberId = 1L;

	@Test
	@WithMockUser
	@DisplayName("회원 프로필 수정 성공 시, 200 OK와 메시지를 반환한다")
	void testUpdateMemberProfile() throws Exception {
		//given
		String newImageUrl = "s3:dagwafd4323d1";

		UpdateMemberProfileRequest request = new UpdateMemberProfileRequest(newImageUrl);

		doNothing()
			.when(memberService)
			.updateMemberProfile(anyLong(), any(UpdateMemberProfileRequest.class));

		String jsonRequest = objectMapper.writeValueAsString(request);

		//when & then
		mockMvc.perform(put("/api/v1/members/" + testMemberId + "/profile")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest))
			.andExpect(status().is(MEMBER_PROFILE_UPDATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(MEMBER_PROFILE_UPDATED.getCode()))
			.andExpect(jsonPath("$.msg").value(MEMBER_PROFILE_UPDATED.getMsg()));

		//verify
		verify(memberService, times(1)).updateMemberProfile(anyLong(), any(UpdateMemberProfileRequest.class));
	}

	@Test
	@WithMockUser
	@DisplayName("회원 소개 수정 성공 시, 200 OK와 메시지를 반환한다")
	void testUpdateMemberIntroduction() throws Exception {
		//given
		String newIntroduction = "bye, i'm hongjugwang.";

		UpdateMemberIntroductionRequest request = new UpdateMemberIntroductionRequest(newIntroduction);

		doNothing()
			.when(memberService)
			.updateMemberIntroduction(anyLong(), any(UpdateMemberIntroductionRequest.class));

		String jsonRequest = objectMapper.writeValueAsString(request);

		//when & then
		mockMvc.perform(put("/api/v1/members/" + testMemberId + "/introduction")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest))
			.andExpect(status().is(MEMBER_INTRODUCTION_UPDATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(MEMBER_INTRODUCTION_UPDATED.getCode()))
			.andExpect(jsonPath("$.msg").value(MEMBER_INTRODUCTION_UPDATED.getMsg()));

		//verify
		verify(memberService, times(1)).updateMemberIntroduction(anyLong(), any(UpdateMemberIntroductionRequest.class));
	}

	@Test
	@WithMockUser
	@DisplayName("회원 비밀번호 수정 성공 시, 200 OK와 메시지를 반환한다")
	void testUpdateMemberPassword() throws Exception {
		//given
		String newPassword = "new_hashed_password";

		doNothing()
			.when(memberService)
			.updateMemberPassword(anyLong(), anyString());

		//when & then
		mockMvc.perform(put("/api/v1/members/" + testMemberId + "/password")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(newPassword))
			.andExpect(status().is(MEMBER_PASSWORD_UPDATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(MEMBER_PASSWORD_UPDATED.getCode()))
			.andExpect(jsonPath("$.msg").value(MEMBER_PASSWORD_UPDATED.getMsg()));

		//verify
		verify(memberService, times(1)).updateMemberPassword(anyLong(), anyString());
	}

	@Test
	@WithMockUser
	@DisplayName("존재하지 않는 회원 정보 조회 시, 404 Not Found와 이유를 반환한다.")
	void testFindMemberDoNotExist() throws Exception {
		//given
		doThrow(new BusinessException(MEMBER_NOT_FOUND))
			.when(memberService)
			.findMember(anyLong());

		//when & then
		mockMvc.perform(get("/api/v1/members/" + testMemberId)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"))
			.andExpect(status().is(MEMBER_NOT_FOUND.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(MEMBER_NOT_FOUND.getCode()))
			.andExpect(jsonPath("$.msg").value(MEMBER_NOT_FOUND.getMsg()));

		//verify
		verify(memberService, times(1)).findMember(anyLong());
	}

	@Test
	@WithMockUser
	@DisplayName("회원 정보 조회 시, 200 OK, 메시지, 회원정보를 반환한다.")
	void testFindMember() throws Exception {
		//given
		doReturn(FindMemberResponse.from(testMember))
			.when(memberService)
			.findMember(anyLong());

		//when & then
		mockMvc.perform(get("/api/v1/members/" + testMemberId)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"))
			.andExpect(status().is(MEMBER_FOUND.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(MEMBER_FOUND.getCode()))
			.andExpect(jsonPath("$.msg").value(MEMBER_FOUND.getMsg()));

		//verify
		verify(memberService, times(1)).findMember(anyLong());
	}

	@Test
	@WithMockUser
	@DisplayName("회원 탈퇴 성공 시, 200 OK와 메시지를 반환한다.")
	void testDeleteMember() throws Exception {
		//given
		doNothing()
			.when(memberService)
			.deleteMember(anyLong());

		//when & then
		mockMvc.perform(delete("/api/v1/members/" + testMemberId)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"))
			.andExpect(status().is(MEMBER_DELETED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(MEMBER_DELETED.getCode()))
			.andExpect(jsonPath("$.msg").value(MEMBER_DELETED.getMsg()));

		//verify
		verify(memberService, times(1)).deleteMember(anyLong());
	}
}
