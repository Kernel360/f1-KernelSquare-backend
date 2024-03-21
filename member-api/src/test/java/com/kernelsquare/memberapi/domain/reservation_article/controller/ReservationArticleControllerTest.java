package com.kernelsquare.memberapi.domain.reservation_article.controller;

import static com.kernelsquare.core.common_response.response.code.ReservationArticleResponseCode.*;
import static com.kernelsquare.memberapi.config.ApiDocumentUtils.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.memberapi.config.RestDocsControllerTest;
import com.kernelsquare.memberapi.domain.reservation_article.dto.CreateReservationArticleRequest;
import com.kernelsquare.memberapi.domain.reservation_article.dto.CreateReservationArticleResponse;
import com.kernelsquare.memberapi.domain.reservation_article.dto.FindAllReservationArticleResponse;
import com.kernelsquare.memberapi.domain.reservation_article.dto.FindReservationArticleResponse;
import com.kernelsquare.memberapi.domain.reservation_article.dto.UpdateReservationArticleRequest;
import com.kernelsquare.memberapi.domain.reservation_article.service.ReservationArticleService;

@DisplayName("커피챗 예약창 컨트롤러 테스트")
@WebMvcTest(ReservationArticleController.class)
class ReservationArticleControllerTest extends RestDocsControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ReservationArticleService reservationArticleService;
	private ObjectMapper objectMapper = new ObjectMapper().setPropertyNamingStrategy(
			PropertyNamingStrategies.SNAKE_CASE)
		.registerModule(new JavaTimeModule());

	@Test
	@WithMockUser
	@DisplayName("예약창 생성 성공시 200 OK 와 응답 메시지를 반환한다.")
	void testCreateReservationArticle() throws Exception {
		//given
		CreateReservationArticleResponse response = CreateReservationArticleResponse.builder()
			.reservationArticleId(1L)
			.build();

		CreateReservationArticleRequest request = CreateReservationArticleRequest.builder()
			.memberId(1L)
			.title("testplz")
			.introduction("dawdda1r21r12r2w")
			.content("ahahahahahhdawdhh")
			.hashTags(List.of("ㄷㅎㅂㅎㅂ", "gqeg"))
			.dateTimes(List.of()).build();

		doReturn(response)
			.when(reservationArticleService)
			.createReservationArticle(any(CreateReservationArticleRequest.class));

		String jsonRequest = objectMapper.writeValueAsString(request);

		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/coffeechat/posts")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8")
			.content(jsonRequest));

		//then
		resultActions
			.andExpect(status().is(RESERVATION_ARTICLE_CREATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("reservation-article-created", getDocumentResponse(),
				requestFields(
					fieldWithPath("member_id").type(JsonFieldType.NUMBER).description("작성자 ID"),
					fieldWithPath("title").type(JsonFieldType.STRING).description("예약창 제목"),
					fieldWithPath("introduction").type(JsonFieldType.STRING).description("예약창 소개"),
					fieldWithPath("content").type(JsonFieldType.STRING).description("예약창 내용"),
					fieldWithPath("hash_tags").type(JsonFieldType.ARRAY).description("해시태그 목록"),
					fieldWithPath("date_times").type(JsonFieldType.ARRAY).description("예약창 일정 목록")
				),
				responseFields(
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
					fieldWithPath("data.reservation_article_id").type(JsonFieldType.NUMBER).description("예약창 ID")
				)));

		//verify
		verify(reservationArticleService, times(1)).createReservationArticle(
			any(CreateReservationArticleRequest.class));
		verifyNoMoreInteractions(reservationArticleService);
	}

	@Test
	@WithMockUser
	@DisplayName("예약창 목록 조회 성공시 200 OK 와 응답 메시지를 반환한다.")
	void testFindAllReservationArticle() throws Exception {
		//given
		FindAllReservationArticleResponse response = FindAllReservationArticleResponse.builder()
			.articleId(1L)
			.memberId(1L)
			.nickname("test")
			.memberImageUrl("http://test.com")
			.level(1L)
			.levelImageUrl("http://test.com")
			.title("testplz")
			.introduction("awgawgawgawgfgw")
			.articleStatus(true)
			.hashTagList(List.of("ㄷㅎㅂㅎㅂ", "gqeg"))
			.coffeeChatCount(1L)
			.availableReservationCount(1L)
			.totalReservationCount(1L)
			.createdDate(LocalDateTime.now())
			.modifiedDate(LocalDateTime.now())
			.build();

		List<FindAllReservationArticleResponse> findAllResponseList = new ArrayList<>(List.of(response));

		Page<FindAllReservationArticleResponse> page = new PageImpl<>(findAllResponseList);
		Pageable pageable = Pageable.ofSize(1);

		PageResponse pageResponse = PageResponse.of(pageable, page, findAllResponseList);

		doReturn(pageResponse)
			.when(reservationArticleService)
			.findAllReservationArticle(any(Pageable.class));

		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/coffeechat/posts")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"));

		//then
		resultActions
			.andExpect(status().is(RESERVATION_ARTICLE_ALL_FOUND.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("reservation-article-all-found", getDocumentResponse(),
				responseFields(
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
					fieldWithPath("data.pagination").type(JsonFieldType.OBJECT).description("페이징 정보"),
					fieldWithPath("data.pagination.total_page").type(JsonFieldType.NUMBER).description("총 페이지 수"),
					fieldWithPath("data.pagination.pageable").type(JsonFieldType.NUMBER).description("페이지 정보"),
					fieldWithPath("data.pagination.is_end").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
					fieldWithPath("data.list").type(JsonFieldType.ARRAY).description("예약창 목록"),
					fieldWithPath("data.list[].article_id").type(JsonFieldType.NUMBER).description("예약창 ID"),
					fieldWithPath("data.list[].member_id").type(JsonFieldType.NUMBER).description("작성자 ID"),
					fieldWithPath("data.list[].nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
					fieldWithPath("data.list[].member_image_url").type(JsonFieldType.STRING)
						.description("작성자 프로필 이미지 URL"),
					fieldWithPath("data.list[].level").type(JsonFieldType.NUMBER).description("작성자 레벨"),
					fieldWithPath("data.list[].level_image_url").type(JsonFieldType.STRING)
						.description("작성자 레벨 이미지 URL"),
					fieldWithPath("data.list[].title").type(JsonFieldType.STRING).description("예약창 제목"),
					fieldWithPath("data.list[].introduction").type(JsonFieldType.STRING).description("예약창 소개"),
					fieldWithPath("data.list[].hash_tag_list").type(JsonFieldType.ARRAY).description("해시태그 목록"),
					fieldWithPath("data.list[].coffee_chat_count").type(JsonFieldType.NUMBER).description("커피챗 수"),
					fieldWithPath("data.list[].available_reservation_count").type(JsonFieldType.NUMBER)
						.description("예약 가능 수"),
					fieldWithPath("data.list[].total_reservation_count").type(JsonFieldType.NUMBER)
						.description("총 예약 수"),
					fieldWithPath("data.list[].created_date").type(JsonFieldType.STRING).description("작성일"),
					fieldWithPath("data.list[].modified_date").type(JsonFieldType.STRING).description("수정일"),
					fieldWithPath("data.list[].article_status").type(JsonFieldType.BOOLEAN).description("예약창 상태")
				)));

		//verify
		verify(reservationArticleService, times(1)).findAllReservationArticle(
			any(Pageable.class));
		verifyNoMoreInteractions(reservationArticleService);
	}

	@Test
	@WithMockUser
	@DisplayName("예약창 조회 성공시 200 OK 와 응답 메시지를 반환한다.")
	void testFindReservationArticle() throws Exception {
		//given
		FindReservationArticleResponse response = FindReservationArticleResponse.builder()
			.articleId(1L)
			.memberId(1L)
			.nickname("test")
			.memberImageUrl("http://test.com")
			.level(1L)
			.levelImageUrl("http://test.com")
			.title("testplz")
			.content("awgawgawgawgfgw")
			.hashtags(List.of())
			.dateTimes(List.of())
			.createdDate(LocalDateTime.now())
			.modifiedDate(LocalDateTime.now())
			.build();

		doReturn(response)
			.when(reservationArticleService)
			.findReservationArticle(anyLong());

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/api/v1/coffeechat/posts/{postId}", 1L)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"));

		//then
		resultActions
			.andExpect(status().is(RESERVATION_ARTICLE_FOUND.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("reservation-article-found", getDocumentResponse(),
				pathParameters(
					parameterWithName("postId").description("예약창 ID")
				),
				responseFields(
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
					fieldWithPath("data.article_id").type(JsonFieldType.NUMBER).description("예약창 ID"),
					fieldWithPath("data.member_id").type(JsonFieldType.NUMBER).description("작성자 ID"),
					fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
					fieldWithPath("data.member_image_url").type(JsonFieldType.STRING)
						.description("작성자 프로필 이미지 URL"),
					fieldWithPath("data.level").type(JsonFieldType.NUMBER).description("작성자 레벨"),
					fieldWithPath("data.level_image_url").type(JsonFieldType.STRING)
						.description("작성자 레벨 이미지 URL"),
					fieldWithPath("data.title").type(JsonFieldType.STRING).description("예약창 제목"),
					fieldWithPath("data.content").type(JsonFieldType.STRING).description("예약창 내용"),
					fieldWithPath("data.hashtags").type(JsonFieldType.ARRAY).description("해시태그 목록"),
					fieldWithPath("data.date_times").type(JsonFieldType.ARRAY).description("예약창 일정 목록"),
					fieldWithPath("data.created_date").type(JsonFieldType.STRING).description("작성일"),
					fieldWithPath("data.modified_date").type(JsonFieldType.STRING).description("수정일")
				)));

		//verify
		verify(reservationArticleService, times(1)).findReservationArticle(
			anyLong());
		verifyNoMoreInteractions(reservationArticleService);
	}

	@Test
	@WithMockUser
	@DisplayName("예약창 수정 성공시 200 OK 와 응답 메시지를 반환한다.")
	void testUpdateReservationArticle() throws Exception {
		//given
		UpdateReservationArticleRequest request = UpdateReservationArticleRequest.builder()
			.memberId(1L)
			.articleId(1L)
			.title("testplz")
			.introduction("dawdda1r21r12r2w")
			.content("ahahahahahhdawdhh")
			.changeHashtags(List.of())
			.changeReservations(List.of())
			.build();

		doNothing()
			.when(reservationArticleService)
			.updateReservationArticle(anyLong(), any(UpdateReservationArticleRequest.class));

		String jsonRequest = objectMapper.writeValueAsString(request);

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.put("/api/v1/coffeechat/posts/{postId}", 1L)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest));

		//then
		resultActions
			.andExpect(status().is(RESERVATION_ARTICLE_UPDATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("reservation-article-updated", getDocumentResponse(),
				pathParameters(
					parameterWithName("postId").description("예약창 ID")
				),
				requestFields(
					fieldWithPath("article_id").type(JsonFieldType.NUMBER).description("예약창 ID"),
					fieldWithPath("member_id").type(JsonFieldType.NUMBER).description("작성자 ID"),
					fieldWithPath("title").type(JsonFieldType.STRING).description("예약창 제목"),
					fieldWithPath("introduction").type(JsonFieldType.STRING).description("예약창 소개"),
					fieldWithPath("content").type(JsonFieldType.STRING).description("예약창 내용"),
					fieldWithPath("change_hashtags").type(JsonFieldType.ARRAY).description("해시태그 목록"),
					fieldWithPath("change_reservations").type(JsonFieldType.ARRAY).description("예약창 일정 목록")
				),
				responseFields(
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드")
				)));

		//verify
		verify(reservationArticleService, times(1)).updateReservationArticle(
			anyLong(), any(UpdateReservationArticleRequest.class));
		verifyNoMoreInteractions(reservationArticleService);
	}

	@Test
	@WithMockUser
	@DisplayName("예약창 삭제 성공시 200 OK 와 응답 메시지를 반환한다.")
	void testDeleteReservationArticle() throws Exception {
		//given
		doNothing()
			.when(reservationArticleService)
			.deleteReservationArticle(anyLong());

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.delete("/api/v1/coffeechat/posts/{postId}", 1L)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"));

		//then
		resultActions
			.andExpect(status().is(RESERVATION_ARTICLE_DELETED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("reservation-article-deleted", getDocumentResponse(),
				pathParameters(
					parameterWithName("postId").description("예약창 ID")
				),
				responseFields(
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드")
				)));

		//verify
		verify(reservationArticleService, times(1)).deleteReservationArticle(
			anyLong());
		verifyNoMoreInteractions(reservationArticleService);
	}
}