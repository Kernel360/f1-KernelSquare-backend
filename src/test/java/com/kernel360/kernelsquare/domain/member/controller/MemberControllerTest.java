package com.kernel360.kernelsquare.domain.member.controller;

import static com.kernel360.kernelsquare.global.common_response.error.code.MemberErrorCode.*;
import static com.kernel360.kernelsquare.global.common_response.response.code.MemberResponseCode.*;
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
import com.kernel360.kernelsquare.domain.member.dto.FindMemberResponse;
import com.kernel360.kernelsquare.domain.member.dto.UpdateMemberRequest;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.member.service.MemberService;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;

@DisplayName("회원 컨트롤러 단위 테스트")
@WebMvcTest(MemberController.class)
public class MemberControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private MemberService memberService;

	private ObjectMapper objectMapper = new ObjectMapper();

	private Member testMember = Member
		.builder()
		.nickname("hongjugwang")
		.email("jugwang@naver.com")
		.password("hashedPassword")
		.accountStatus(false)
		.experience(10000L)
		.introduction("hi, i'm hongjugwang.")
		.imageUrl("s3:qwe12fasdawczx")
		.build();

	private Long testMemberId = 1L;

	@Test
	@WithMockUser
	@DisplayName("회원 정보 수정 성공 시, 200 OK와 메시지를 반환한다")
	void testUpdateMember() throws Exception {
		//given
		String newImageUrl = "s3:dagwafd4323d1";
		String newIntroduction = "bye, i'm hongjugwang.";

		UpdateMemberRequest request = new UpdateMemberRequest(newImageUrl, newIntroduction);

		doNothing()
			.when(memberService)
			.updateMember(anyLong(), any(UpdateMemberRequest.class));

		String jsonRequest = objectMapper.writeValueAsString(request);

		//when & then
		mockMvc.perform(put("/api/v1/members/" + testMemberId)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(MEMBER_INFO_UPDATED.getStatus().value()))
			.andExpect(jsonPath("$.msg").value(MEMBER_INFO_UPDATED.getMsg()));

		//verify
		verify(memberService, times(1)).updateMember(anyLong(), any(UpdateMemberRequest.class));
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
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(MEMBER_PASSWORD_UPDATED.getStatus().value()))
			.andExpect(jsonPath("$.msg").value(MEMBER_PASSWORD_UPDATED.getMsg()));

		//verify
		verify(memberService, times(1)).updateMemberPassword(anyLong(), anyString());
	}

	@Test
	@WithMockUser
	@DisplayName("존재하지 않는 회원 정보 조회 시, 404 Not Found와 이유를 반환한다.")
	void testFindMemberDoNotExist() throws Exception {
		//given
		doThrow(new BusinessException(NOT_FOUND_MEMBER))
			.when(memberService)
			.findMember(anyLong());

		//when & then
		mockMvc.perform(get("/api/v1/members/" + testMemberId)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"))
			.andExpect(status().isNotFound())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(NOT_FOUND_MEMBER.getStatus().value()))
			.andExpect(jsonPath("$.msg").value(NOT_FOUND_MEMBER.getMsg()));

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
			.findMember(testMemberId);

		//when & then
		mockMvc.perform(get("/api/v1/members/" + testMemberId)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(MEMBER_FOUND.getStatus().value()))
			.andExpect(jsonPath("$.msg").value(MEMBER_FOUND.getMsg()))
			.andExpect(jsonPath("$.data.nickname").value(testMember.getNickname()))
			.andExpect(jsonPath("$.data.experience").value(testMember.getExperience()))
			.andExpect(jsonPath("$.data.introduction").value(testMember.getIntroduction()))
			.andExpect(jsonPath("$.data.image_url").value(testMember.getImageUrl()));

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
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(MEMBER_DELETED.getStatus().value()))
			.andExpect(jsonPath("$.msg").value(MEMBER_DELETED.getMsg()));

		//verify
		verify(memberService, times(1)).deleteMember(anyLong());
	}
}
