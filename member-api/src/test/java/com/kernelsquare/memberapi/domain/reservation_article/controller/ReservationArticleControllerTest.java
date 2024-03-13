package com.kernelsquare.memberapi.domain.reservation_article.controller;

import static com.kernelsquare.core.common_response.response.code.ReservationArticleResponseCode.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.core.dto.Pagination;
import com.kernelsquare.domainmysql.domain.hashtag.entity.Hashtag;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.reservation_article.entity.ReservationArticle;
import com.kernelsquare.memberapi.domain.hashtag.dto.FindHashtagResponse;
import com.kernelsquare.memberapi.domain.reservation.dto.FindReservationResponse;
import com.kernelsquare.memberapi.domain.reservation_article.dto.CreateReservationArticleRequest;
import com.kernelsquare.memberapi.domain.reservation_article.dto.CreateReservationArticleResponse;
import com.kernelsquare.memberapi.domain.reservation_article.dto.FindAllReservationArticleResponse;
import com.kernelsquare.memberapi.domain.reservation_article.dto.FindReservationArticleResponse;
import com.kernelsquare.memberapi.domain.reservation_article.service.ReservationArticleService;

@DisplayName("커피챗 예약창 컨트롤러 단위 테스트")
@WebMvcTest(ReservationArticleController.class)
class ReservationArticleControllerTest {
	private final ObjectMapper objectMapper = new ObjectMapper();
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ReservationArticleService reservationArticleService;

	private Member member;
	private Level level;

	private ReservationArticle createTestReservationArticle(Long id) {
		return ReservationArticle.builder()
			.id(id)
			.title("testplz")
			.introduction("dawfgawgawgawgawg")
			.content("ahahahahahhhh")
			.build();
	}

	private Member createTestMember() {
		return Member.builder()
			.id(1L)
			.nickname("hongjugwang")
			.email("jugwang@naver.com")
			.password("hashedPassword")
			.experience(10000L)
			.introduction("hi, i'm hongjugwang.")
			.imageUrl("s3:qwe12fasdawczx")
			.level(level)
			.build();
	}

	private Level createTestLevel() {
		return Level.builder()
			.name(6L)
			.imageUrl("1.jpg")
			.build();
	}

	private Hashtag createTestHashtag() {
		return Hashtag.builder()
			.content("#tester333")
			.build();
	}

	@Test
	@WithMockUser
	@DisplayName("예약창 생성 성공시 200 OK 와 응답 메시지를 반환한다.")
	void testCreateReservationArticle() throws Exception {
		// Given
		member = createTestMember();
		ReservationArticle reservationArticle = createTestReservationArticle(1L);
		Hashtag hashtag = createTestHashtag();

		CreateReservationArticleRequest createReservationArticleRequest =
			new CreateReservationArticleRequest(member.getId(), reservationArticle.getTitle(),
				reservationArticle.getContent(),
				reservationArticle.getIntroduction(),
				List.of(hashtag.getContent()), List.of(LocalDateTime.now(), LocalDateTime.now().plusDays(2)));

		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		objectMapper.registerModule(new JavaTimeModule());
		String jsonRequest = objectMapper.writeValueAsString(createReservationArticleRequest);

		given(
			reservationArticleService.createReservationArticle(any(CreateReservationArticleRequest.class))).willReturn(
			CreateReservationArticleResponse.from(reservationArticle));

		// When & Then
		mockMvc.perform(post("/api/v1/coffeechat/posts")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest))
			.andExpect(status().is(RESERVATION_ARTICLE_CREATED.getStatus().value()))
			.andExpect(jsonPath("$.code").value(RESERVATION_ARTICLE_CREATED.getCode()))
			.andExpect(jsonPath("$.msg").value(RESERVATION_ARTICLE_CREATED.getMsg()));

		// Verify
		verify(reservationArticleService, times(1)).createReservationArticle(
			any(CreateReservationArticleRequest.class));
	}

	@Test
	@WithMockUser
	@DisplayName("모든 예약창 조회 성공시 200 OK 와 응답 메시지를 반환한다.")
	void testFindAllReservationArticle() throws Exception {
		// Given
		level = createTestLevel();
		member = createTestMember();

		Hashtag hashtag1 = createTestHashtag();
		Hashtag hashtag2 = createTestHashtag();

		List<Hashtag> hashtagList = List.of(hashtag1, hashtag2);

		ReservationArticle reservationArticle1 = ReservationArticle.builder()
			.id(1L)
			.member(member)
			.title("testplz")
			.content("ahahahahahhhh")
			.hashtagList(hashtagList)
			.startTime(LocalDateTime.now())
			.build();

		ReservationArticle reservationArticle2 = ReservationArticle.builder()
			.id(2L)
			.member(member)
			.title("testplz22")
			.content("ahahahahahhhh2222")
			.hashtagList(hashtagList)
			.startTime(LocalDateTime.now())
			.build();

		Pageable pageable = PageRequest.of(0, 2);
		Pagination pagination = Pagination.builder()
			.totalPage(1)
			.pageable(pageable.getPageSize())
			.isEnd(true)
			.build();

		FindAllReservationArticleResponse findAllReservationArticleResponse1 = FindAllReservationArticleResponse.of(
			member, reservationArticle1, true, 1L, 1L, 1L);
		FindAllReservationArticleResponse findAllReservationArticleResponse2 = FindAllReservationArticleResponse.of(
			member, reservationArticle2, false, 0L, 1L, 1L);

		List<FindAllReservationArticleResponse> responsePages = List.of(findAllReservationArticleResponse1,
			findAllReservationArticleResponse2);

		PageResponse<FindAllReservationArticleResponse> response = PageResponse.of(pagination, responsePages);

		doReturn(response)
			.when(reservationArticleService)
			.findAllReservationArticle(any(Pageable.class));

		// When & Then
		mockMvc.perform(get("/api/v1/coffeechat/posts")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"))
			.andExpect(status().is(RESERVATION_ARTICLE_ALL_FOUND.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(RESERVATION_ARTICLE_ALL_FOUND.getCode()))
			.andExpect(jsonPath("$.msg").value(RESERVATION_ARTICLE_ALL_FOUND.getMsg()))
			.andExpect(jsonPath("$.data.pagination.total_page").value(1))
			.andExpect(jsonPath("$.data.pagination.pageable").value(pageable.getPageSize()))
			.andExpect(jsonPath("$.data.pagination.is_end").value(true));

		// Verify
		verify(reservationArticleService, times(1)).findAllReservationArticle(any(PageRequest.class));
	}

	@Test
	@WithMockUser
	@DisplayName("예약창 조회 성공시 200 OK 와 응답 메시지를 반환한다.")
	void testFindReservationArticle() throws Exception {
		// Given
		level = createTestLevel();
		member = createTestMember();
		ReservationArticle reservationArticle = createTestReservationArticle(1L);

		FindReservationResponse findReservationResponse1 = FindReservationResponse.builder()
			.startTime(LocalDateTime.now())
			.menteeNickname("tester22")
			.menteeImageUrl("url")
			.build();

		FindReservationResponse findReservationResponse2 = FindReservationResponse.builder()
			.startTime(LocalDateTime.now())
			.menteeNickname("tester23")
			.menteeImageUrl("url")
			.build();

		FindHashtagResponse findHashtagResponse1 = FindHashtagResponse.builder()
			.hashtagId(1L)
			.content("#JYP")
			.build();

		FindHashtagResponse findHashtagResponse2 = FindHashtagResponse.builder()
			.hashtagId(2L)
			.content("#YG")
			.build();

		List<FindReservationResponse> findReservationResponseList = List.of(findReservationResponse1,
			findReservationResponse2);
		List<FindHashtagResponse> findHashtagResponseList = List.of(findHashtagResponse1, findHashtagResponse2);

		FindReservationArticleResponse findReservationArticleResponse = FindReservationArticleResponse.of(member,
			reservationArticle, findHashtagResponseList, findReservationResponseList);

		given(reservationArticleService.findReservationArticle(anyLong())).willReturn(findReservationArticleResponse);

		// When & Then
		mockMvc.perform(get("/api/v1/coffeechat/posts/" + reservationArticle.getId())
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"))
			.andExpect(status().is(RESERVATION_ARTICLE_FOUND.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(RESERVATION_ARTICLE_FOUND.getCode()))
			.andExpect(jsonPath("$.msg").value(RESERVATION_ARTICLE_FOUND.getMsg()));

		// Verify
		verify(reservationArticleService, times(1)).findReservationArticle(anyLong());

	}

	@Test
	@WithMockUser
	@DisplayName("예약창 삭제 성공시 200 OK 와 응답 메시지를 반환한다.")
	void testDeleteReservationArticle() throws Exception {
		// Given
		level = createTestLevel();
		member = createTestMember();
		ReservationArticle reservationArticle = createTestReservationArticle(1L);

		doNothing()
			.when(reservationArticleService)
			.deleteReservationArticle(anyLong());

		// When & Then
		mockMvc.perform(delete("/api/v1/coffeechat/posts/" + reservationArticle.getId())
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"))
			.andExpect(status().is(RESERVATION_ARTICLE_DELETED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(RESERVATION_ARTICLE_DELETED.getCode()))
			.andExpect(jsonPath("$.msg").value(RESERVATION_ARTICLE_DELETED.getMsg()));

		// Verify
		verify(reservationArticleService, times(1)).deleteReservationArticle(anyLong());
	}

}