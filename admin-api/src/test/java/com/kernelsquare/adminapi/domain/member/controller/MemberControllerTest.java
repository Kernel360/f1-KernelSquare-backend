package com.kernelsquare.adminapi.domain.member.controller;

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
import com.kernelsquare.adminapi.domain.member.dto.FindMemberResponse;
import com.kernelsquare.adminapi.domain.member.service.MemberService;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;

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
