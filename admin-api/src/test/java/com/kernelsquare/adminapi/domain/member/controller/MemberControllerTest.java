package com.kernelsquare.adminapi.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kernelsquare.adminapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.adminapi.domain.auth.dto.MemberAdaptorInstance;
import com.kernelsquare.adminapi.domain.member.dto.MemberDto;
import com.kernelsquare.adminapi.domain.member.facade.MemberFacade;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.type.AuthorityType;
import com.kernelsquare.core.util.ImageUtils;
import com.kernelsquare.domainmysql.domain.authority.entity.Authority;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.kernelsquare.adminapi.config.ApiDocumentUtils.getDocumentRequest;
import static com.kernelsquare.adminapi.config.ApiDocumentUtils.getDocumentResponse;
import static com.kernelsquare.core.common_response.error.code.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.kernelsquare.core.common_response.response.code.MemberResponseCode.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("회원 컨트롤러 단위 테스트")
@WebMvcTest(MemberController.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
public class MemberControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private MemberFacade memberFacade;

	private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
		.setPropertyNamingStrategy(PropertyNamingStrategies.SnakeCaseStrategy.INSTANCE);

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
			.when(memberFacade)
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
		verify(memberFacade, times(1)).findMember(anyLong());
	}

	@Test
	@WithMockUser
	@DisplayName("회원 정보 조회 시, 200 OK, 메시지, 회원정보를 반환한다.")
	void testFindMember() throws Exception {
		//given
		MemberDto.FindResponse response = MemberDto.FindResponse.builder()
			.memberId(testMemberId)
			.experience(testMember.getExperience())
			.imageUrl(ImageUtils.makeImageUrl(testMember.getImageUrl()))
			.introduction(testMember.getIntroduction())
			.nickname(testMember.getNickname())
			.level(testMember.getLevel().getName())
			.build();

		doReturn(response)
			.when(memberFacade)
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
		verify(memberFacade, times(1)).findMember(anyLong());
	}

	@Test
	@WithMockUser
	@DisplayName("회원 탈퇴 성공 시, 200 OK와 메시지를 반환한다.")
	void testDeleteMember() throws Exception {
		//given
		doNothing()
			.when(memberFacade)
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
		verify(memberFacade, times(1)).deleteMember(anyLong());
	}

	@Test
	@WithMockUser
	@DisplayName("회원 권한 수정 성공 시, 200 OK와 메시지를 반환한다.")
	void testUpdateMemberAuthority() throws Exception {
		//given
		Member member = Member.builder()
			.id(1L)
			.nickname("machine")
			.email("awdag@nsavasc.om")
			.password("hashed")
			.experience(1200L)
			.introduction("basfas")
			.authorities(List.of(
				MemberAuthority.builder()
					.member(Member.builder().build())
					.authority(Authority.builder().authorityType(AuthorityType.ROLE_USER).build())
					.build()))
			.imageUrl("agawsc")
			.build();

		MemberAdapter memberAdapter = new MemberAdapter(MemberAdaptorInstance.of(member));

		MemberDto.UpdateAuthorityRequest request = MemberDto.UpdateAuthorityRequest.builder()
			.memberId(member.getId())
			.authorityType(AuthorityType.ROLE_MENTOR.getDescription())
			.build();

		String jsonRequest = objectMapper.writeValueAsString(request);

		doNothing()
			.when(memberFacade)
			.updateMemberAuthority(any(MemberDto.UpdateAuthorityRequest.class));

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.put("/api/v1/members/role")
				.with(csrf())
				.with(user(memberAdapter))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest));

		//then
		resultActions
			.andExpect(status().is(MEMBER_AUTHORITY_UPDATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("member-authority-update",
				getDocumentRequest(),
				getDocumentResponse(),
				requestFields(
					fieldWithPath("member_id").type(JsonFieldType.NUMBER).description("회원 id"),
					fieldWithPath("authority_type").type(JsonFieldType.STRING).description("권한")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지")
				)));

		//verify
		verify(memberFacade, times(1)).updateMemberAuthority(any(MemberDto.UpdateAuthorityRequest.class));
	}

	@Test
	@WithMockUser
	@DisplayName("회원 닉네임 수정 성공 시, 200 OK와 메시지를 반환한다.")
	void testUpdateMemberNickname() throws Exception {
		//given
		Level level = Level.builder()
			.id(1L)
			.imageUrl("s3:/level/asdsadasd")
			.name(1L)
			.levelUpperLimit(200L)
			.build();

		Member member = Member.builder()
			.id(1L)
			.nickname("machine")
			.email("awdag@nsavasc.om")
			.password("hashed")
			.experience(1200L)
			.introduction("basfas")
			.authorities(List.of(
				MemberAuthority.builder()
					.member(Member.builder().build())
					.authority(Authority.builder().authorityType(AuthorityType.ROLE_USER).build())
					.build()))
			.imageUrl("agawsc")
			.level(level)
			.build();

		MemberAdapter memberAdapter = new MemberAdapter(MemberAdaptorInstance.of(member));

		MemberDto.UpdateNicknameRequest request = MemberDto.UpdateNicknameRequest.builder()
			.memberId(member.getId())
			.nickname("하이연")
			.build();

		MemberDto.FindResponse response = MemberDto.FindResponse.builder()
			.nickname(member.getNickname())
			.memberId(member.getId())
			.level(member.getLevel().getName())
			.experience(member.getExperience())
			.imageUrl(ImageUtils.makeImageUrl(member.getImageUrl()))
			.introduction(member.getIntroduction())
			.build();

		doReturn(response)
			.when(memberFacade)
			.updateMemberNickname(any(MemberDto.UpdateNicknameRequest.class));

		String jsonRequest = objectMapper.writeValueAsString(request);

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.put("/api/v1/members/nick")
				.with(csrf())
				.with(user(memberAdapter))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest));

		//then
		resultActions
			.andExpect(status().is(MEMBER_NICKNAME_UPDATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("member-nickname-update",
				getDocumentRequest(),
				getDocumentResponse(),
				requestFields(
					fieldWithPath("member_id").type(JsonFieldType.NUMBER).description("회원 id"),
					fieldWithPath("nickname").type(JsonFieldType.STRING).description("회원 닉네임")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답"),
					fieldWithPath("data.member_id").type(JsonFieldType.NUMBER).description("회원 id"),
					fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
					fieldWithPath("data.experience").type(JsonFieldType.NUMBER).description("회원 경험지"),
					fieldWithPath("data.introduction").type(JsonFieldType.STRING).description("회원 자기소개"),
					fieldWithPath("data.image_url").type(JsonFieldType.STRING).description("회원 프로필 이미지"),
					fieldWithPath("data.level").type(JsonFieldType.NUMBER).description("회원 레벨")
				)));

		//verify
		verify(memberFacade, times(1)).updateMemberNickname(any(MemberDto.UpdateNicknameRequest.class));
	}
}
