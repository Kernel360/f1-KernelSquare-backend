package com.kernel360.kernelsquare.domain.reservation_article.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kernel360.kernelsquare.domain.hashtag.entity.Hashtag;
import com.kernel360.kernelsquare.domain.level.entity.Level;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.reservation.dto.ReservationDto;
import com.kernel360.kernelsquare.domain.reservation_article.dto.CreateReservationArticleRequest;
import com.kernel360.kernelsquare.domain.reservation_article.dto.CreateReservationArticleResponse;
import com.kernel360.kernelsquare.domain.reservation_article.dto.FindAllReservationArticleResponse;
import com.kernel360.kernelsquare.domain.reservation_article.dto.FindReservationArticleResponse;
import com.kernel360.kernelsquare.domain.reservation_article.entity.ReservationArticle;
import com.kernel360.kernelsquare.domain.reservation_article.service.ReservationArticleService;
import com.kernel360.kernelsquare.global.dto.PageResponse;
import com.kernel360.kernelsquare.global.dto.Pagination;
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

import java.time.LocalDateTime;
import java.util.List;


import static com.kernel360.kernelsquare.global.common_response.response.code.ReservationArticleResponseCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
                .content("ahahahahahhhh")
                .hashtagList(List.of())
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

    @Test
    @WithMockUser
    @DisplayName("예약창 생성 성공시 200 OK 와 응답 메시지를 반환한다.")
    void testCreateReservationArticle() throws Exception {
        // Given

        member = createTestMember();
        ReservationArticle reservationArticle = createTestReservationArticle(1L);

        CreateReservationArticleRequest createReservationArticleRequest =
                new CreateReservationArticleRequest(member.getId(), reservationArticle.getTitle(), reservationArticle.getContent(),
                reservationArticle.getHashtagList().stream().map(Hashtag::getContent).toList(), List.of(LocalDateTime.now(),LocalDateTime.now().plusDays(2)));

        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.registerModule(new JavaTimeModule());
        String jsonRequest = objectMapper.writeValueAsString(createReservationArticleRequest);


        given(reservationArticleService.createReservationArticle(any(CreateReservationArticleRequest.class))).willReturn(CreateReservationArticleResponse.from(reservationArticle));

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
        verify(reservationArticleService, times(1)).createReservationArticle(any(CreateReservationArticleRequest.class));
    }
    @Test
    @WithMockUser
    @DisplayName("모든 예약창 조회 성공시 200 OK 와 응답 메시지를 반환한다.")
    void testFindAllReservationArticle() throws Exception {
        // Given
        level = createTestLevel();
        member = createTestMember();
        ReservationArticle article1 = createTestReservationArticle(1L);
        ReservationArticle article2 = createTestReservationArticle(2L);

        Pageable pageable = PageRequest.of(0,2);
        Pagination pagination = Pagination.builder()
                .totalPage(1)
                .pageable(pageable.getPageSize())
                .isEnd(true)
                .build();

        FindAllReservationArticleResponse findAllReservationArticleResponse1 = FindAllReservationArticleResponse.of(member, article1 ,1L);
        FindAllReservationArticleResponse findAllReservationArticleResponse2 = FindAllReservationArticleResponse.of(member, article2 ,0L);

        List<FindAllReservationArticleResponse> responsePages = List.of(findAllReservationArticleResponse1, findAllReservationArticleResponse2);

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

        ReservationDto reservationDto1 = ReservationDto.builder()
                .finished(true)
                .startTime(LocalDateTime.now())
                .memberNickname("tester22")
                .memberImageUrl("url")
                .build();

        ReservationDto reservationDto2 = ReservationDto.builder()
                .finished(true)
                .startTime(LocalDateTime.now())
                .memberNickname("tester23")
                .memberImageUrl("url")
                .build();

        List<ReservationDto> reservationDtoList = List.of(reservationDto1, reservationDto2);

        FindReservationArticleResponse findReservationArticleResponse = FindReservationArticleResponse.of(member, reservationArticle, reservationDtoList, level);

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