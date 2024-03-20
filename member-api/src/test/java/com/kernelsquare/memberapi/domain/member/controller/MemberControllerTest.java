package com.kernelsquare.memberapi.domain.member.controller;

import static com.kernelsquare.core.common_response.response.code.MemberResponseCode.*;
import static com.kernelsquare.memberapi.config.ApiDocumentUtils.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.memberapi.config.RestDocsControllerTest;
import com.kernelsquare.memberapi.domain.member.dto.FindMemberResponse;
import com.kernelsquare.memberapi.domain.member.dto.UpdateMemberIntroductionRequest;
import com.kernelsquare.memberapi.domain.member.dto.UpdateMemberProfileRequest;
import com.kernelsquare.memberapi.domain.member.service.MemberService;

@DisplayName("회원 컨트롤러 테스트")
@WithMockUser
@WebMvcTest(MemberController.class)
class MemberControllerTest extends RestDocsControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private MemberService memberService;

	private ObjectMapper objectMapper = new ObjectMapper().setPropertyNamingStrategy(
		PropertyNamingStrategies.SNAKE_CASE);

	private Level testLevel = Level.builder()
		.id(1L)
		.name(1L)
		.imageUrl("s3:dq1234512")
		.build();

	private Member testMember = Member
		.builder()
		.id(1L)
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
	@DisplayName("회원 프로필 수정 성공 시, 200 OK와 메시지를 반환한다")
	void testUpdateMemberProfile() throws Exception {
		//given
		String newImageUrl = "s3:dagwafd4323d1";

		UpdateMemberProfileRequest request = new UpdateMemberProfileRequest(newImageUrl);

		doNothing()
			.when(memberService)
			.updateMemberProfile(anyLong(), any(UpdateMemberProfileRequest.class));

		String jsonRequest = objectMapper.writeValueAsString(request);

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.put("/api/v1/members/" + testMemberId + "/profile")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest));

		//then
		resultActions
			.andExpect(status().is(MEMBER_PROFILE_UPDATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("member-profile-updated", getDocumentRequest(), getDocumentResponse()
				, requestFields(
					fieldWithPath("image_url").type(JsonFieldType.STRING).description("프로필 이미지 URL")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("커스텀 응답 코드"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지")
				)));

		//verify
		verify(memberService, times(1)).updateMemberProfile(anyLong(), any(UpdateMemberProfileRequest.class));
		verifyNoMoreInteractions(memberService);
	}

	@Test
	@WithMockUser
	@DisplayName("회원 소개 수정 성공 시, 200 OK와 메시지를 반환한다")
	void testUpdateMemberIntroduction() throws Exception {
		//given
		String newIntroduction = "hi, i'm hongjugwang. nice to meet you.";

		UpdateMemberIntroductionRequest request = new UpdateMemberIntroductionRequest(newIntroduction);

		String jsonRequest = objectMapper.writeValueAsString(request);

		doNothing()
			.when(memberService)
			.updateMemberIntroduction(anyLong(), any(UpdateMemberIntroductionRequest.class));

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.put("/api/v1/members/" + testMemberId + "/introduction")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest));

		//then
		resultActions
			.andExpect(status().is(MEMBER_INTRODUCTION_UPDATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("member-introduction-updated", getDocumentRequest(), getDocumentResponse()
				, requestFields(
					fieldWithPath("introduction").type(JsonFieldType.STRING).description("회원 소개")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("커스텀 응답 코드"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지")
				)));

		//verify
		verify(memberService, times(1))
			.updateMemberIntroduction(anyLong(), any(UpdateMemberIntroductionRequest.class));
		verifyNoMoreInteractions(memberService);
	}

	@Test
	@DisplayName("회원 비밀번호 수정 성공 시, 200 OK와 메시지를 반환한다")
	void testUpdateMemberPassword() throws Exception {
		//given
		String newPassword = "newPassword";

		String jsonRequest = "{\"password\": \"" + newPassword + "\"}";

		doNothing()
			.when(memberService)
			.updateMemberPassword(anyLong(), anyString());

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.put("/api/v1/members/" + testMemberId + "/password")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest));

		//then
		resultActions
			.andExpect(status().is(MEMBER_PASSWORD_UPDATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("member-password-updated", getDocumentRequest(), getDocumentResponse()
				, requestFields(
					fieldWithPath("password").type(JsonFieldType.STRING).description("회원 비밀번호")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("커스텀 응답 코드"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지")
				)));

		//verify
		verify(memberService, times(1)).updateMemberPassword(anyLong(), anyString());
		verifyNoMoreInteractions(memberService);
	}

	@Test
	@DisplayName("회원 조회 성공 시, 200 OK와 회원 정보를 반환한다")
	void testFindMember() throws Exception {
		//given
		given(memberService.findMember(anyLong())).willReturn(FindMemberResponse.from(testMember));

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/api/v1/members/" + testMemberId)
				.accept(MediaType.APPLICATION_JSON));

		//then
		resultActions
			.andExpect(status().is(MEMBER_FOUND.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("member-found", getDocumentRequest(), getDocumentResponse()
				, responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("커스텀 응답 코드"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("data.member_id").type(JsonFieldType.NUMBER).description("회원 ID"),
					fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
					fieldWithPath("data.experience").type(JsonFieldType.NUMBER).description("회원 경험치"),
					fieldWithPath("data.introduction").type(JsonFieldType.STRING).description("회원 소개"),
					fieldWithPath("data.image_url").type(JsonFieldType.STRING).description("회원 프로필 이미지 URL"),
					fieldWithPath("data.level").type(JsonFieldType.NUMBER).description("회원 레벨 ID")
				)));

		//verify
		verify(memberService, times(1)).findMember(anyLong());
		verifyNoMoreInteractions(memberService);
	}

	@Test
	@DisplayName("회원 탈퇴 성공 시, 200 OK와 메시지를 반환한다")
	void testDeleteMember() throws Exception {
		//given
		doNothing()
			.when(memberService)
			.deleteMember(anyLong());

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.delete("/api/v1/members/" + testMemberId)
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON));

		//then
		resultActions
			.andExpect(status().is(MEMBER_DELETED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("member-deleted", getDocumentRequest(), getDocumentResponse()
				, responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("커스텀 응답 코드"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지")
				)));

		//verify
		verify(memberService, times(1)).deleteMember(anyLong());
		verifyNoMoreInteractions(memberService);
	}
}
