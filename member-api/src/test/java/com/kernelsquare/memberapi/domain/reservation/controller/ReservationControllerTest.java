package com.kernelsquare.memberapi.domain.reservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kernelsquare.core.type.AuthorityType;
import com.kernelsquare.domainmysql.domain.authority.entity.Authority;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdaptorInstance;
import com.kernelsquare.memberapi.domain.reservation.dto.AddReservationMemberRequest;
import com.kernelsquare.memberapi.domain.reservation.dto.AddReservationMemberResponse;
import com.kernelsquare.memberapi.domain.reservation.dto.FindAllReservationResponse;
import com.kernelsquare.memberapi.domain.reservation.dto.FindReservationResponse;
import com.kernelsquare.memberapi.domain.reservation.service.ReservationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static com.kernelsquare.core.common_response.response.code.ReservationResponseCode.*;
import static com.kernelsquare.memberapi.config.ApiDocumentUtils.getDocumentRequest;
import static com.kernelsquare.memberapi.config.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("예약 컨트롤러 테스트")
@WebMvcTest(ReservationController.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
class ReservationControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ReservationService reservationService;
	private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
		.setPropertyNamingStrategy(
			PropertyNamingStrategies.SnakeCaseStrategy.INSTANCE);

	@Test
	@DisplayName("모든 예약 조회 성공 시, 200 OK와 메시지, 정상 응답을 반환한다.")
	void testFindAllReservationByMemberId() throws Exception {
		//given
		FindReservationResponse findReservationResponse1 = FindReservationResponse.builder()
			.reservationId(1L)
			.startTime(LocalDateTime.of(2024, 10, 10, 0, 30))
			.menteeNickname("홍주광")
			.menteeImageUrl("s3:12d12dgba")
			.roomId(1L)
			.build();

		FindReservationResponse findReservationResponse2 = FindReservationResponse.builder()
			.reservationId(2L)
			.startTime(LocalDateTime.of(2024, 10, 10, 1, 00))
			.menteeNickname("김원상")
			.menteeImageUrl("s3:122212dgba")
			.roomId(2L)
			.build();

		List<FindReservationResponse> reservationResponses = List.of(findReservationResponse1,
			findReservationResponse2);

		FindAllReservationResponse findAllReservationResponse = FindAllReservationResponse.builder()
			.reservationResponses(reservationResponses).build();

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

		doReturn(findAllReservationResponse)
			.when(reservationService)
			.findAllReservationByMemberId(anyLong());

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/api/v1/coffeechat/reservations")
				.with(csrf())
				.with(user(memberAdapter))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"));

		//then
		resultActions
			.andExpect(status().is(RESERVATION_ALL_FOUND.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("reservation-all-found",
				getDocumentResponse(),
				responseFields(
					fieldWithPath("data").description("응답"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
					fieldWithPath("data.reservation_responses").type(JsonFieldType.ARRAY).description("예약 응답 배열"),
					fieldWithPath("data.reservation_responses[].reservation_id").type(JsonFieldType.NUMBER)
						.description("예약 아이디"),
					//todo : 방 아이디 type 확인 -> Null 맞음?
					fieldWithPath("data.reservation_responses[].room_id").type(JsonFieldType.NUMBER)
						.description("방 아이디"),
					fieldWithPath("data.reservation_responses[].start_time").type(JsonFieldType.STRING)
						.description("커피챗 시작 시간"),
					fieldWithPath("data.reservation_responses[].mentee_nickname").type(JsonFieldType.STRING)
						.description("멘티 닉네임"),
					fieldWithPath("data.reservation_responses[].mentee_image_url").type(JsonFieldType.STRING)
						.description("멘티 프로필 사진 링크")
				)));

		//verify
		verify(reservationService, times(1)).findAllReservationByMemberId(anyLong());
	}

	@Test
	@DisplayName("예약 취소 성공시 200 OK와 메시지를 반환한다.")
	void testDeleteReservation() throws Exception {
		//given
		Long reservationId = 1L;

		doNothing()
			.when(reservationService)
			.deleteReservationMember(anyLong());

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

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.delete("/api/v1/coffeechat/reservations/" + reservationId)
				.with(csrf())
				.with(user(memberAdapter))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"));

		//then
		resultActions.andExpect(status().is(RESERVATION_DELETED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("reservation-deleted",
				getDocumentResponse(),
				responseFields(
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"))));

		//verify
		verify(reservationService, times(1)).deleteReservationMember(anyLong());
	}

	@Test
	@DisplayName("예약 성공 시, 존재하는 예약에 회원을 추가하고 200 OK와 정상 응답을 반환한다.")
	void testAddReservationMember() throws Exception {
		//given
		AddReservationMemberRequest addReservationMemberRequest = AddReservationMemberRequest.builder()
			.reservationArticleId(1L)
			.reservationId(1L)
			.memberId(1L)
			.reservationStartTime(LocalDateTime.now())
			.build();

		AddReservationMemberResponse addReservationMemberResponse = AddReservationMemberResponse
			.builder()
			.reservationArticleId(1L)
			.build();

		doReturn(addReservationMemberResponse)
			.when(reservationService)
			.AddReservationMember(any(AddReservationMemberRequest.class), anyLong());

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

		String jsonRequest = objectMapper.writeValueAsString(addReservationMemberRequest);

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.put("/api/v1/coffeechat/reservations/book")
				.with(csrf())
				.with(user(memberAdapter))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest));

		//then
		resultActions.andExpect(status().is(RESERVATION_SUCCESS.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("reservation-success",
				getDocumentRequest(),
				getDocumentResponse(),
				requestFields(
					fieldWithPath("reservation_article_id").type(JsonFieldType.NUMBER).description("예약 게시글 id"),
					fieldWithPath("reservation_id").type(JsonFieldType.NUMBER).description("예약 아이디"),
					fieldWithPath("member_id").type(JsonFieldType.NUMBER).description("회원 아이디"),
					//todo : 시작 시간 type 확인 -> ARRAY 맞음?
					fieldWithPath("reservation_start_time").type(JsonFieldType.ARRAY).description("시작 시간")
				),
				responseFields(
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
					fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답"),
					fieldWithPath("data.reservation_article_id").type(JsonFieldType.NUMBER)
						.description("예약 게시글 아이디"))));

		//verify
		verify(reservationService, times(1)).AddReservationMember(any(AddReservationMemberRequest.class), anyLong());
	}
}
