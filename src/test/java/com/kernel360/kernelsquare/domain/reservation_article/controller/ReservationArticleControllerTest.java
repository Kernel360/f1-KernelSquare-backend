package com.kernel360.kernelsquare.domain.reservation_article.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kernel360.kernelsquare.domain.hashtag.entity.HashTag;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.reservation_article.dto.CreateReservationArticleRequest;
import com.kernel360.kernelsquare.domain.reservation_article.dto.CreateReservationArticleResponse;
import com.kernel360.kernelsquare.domain.reservation_article.entity.ReservationArticle;
import com.kernel360.kernelsquare.domain.reservation_article.service.ReservationArticleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static com.kernel360.kernelsquare.global.common_response.response.code.ReservationArticleResponseCode.RESERVATION_ARTICLE_CREATED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("커피챗 예약게시글 컨트롤러 단위 테스트")
@WebMvcTest(ReservationArticleController.class)
class ReservationArticleControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReservationArticleService reservationArticleService;

    private Member member;

    private ReservationArticle createTestReservationArticle(Long id) {
        return ReservationArticle.builder()
                .id(id)
                .title("testplz")
                .content("ahahahahahhhh")
                .hashTagList(List.of())
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
            .build();
    }

    @Test
    @WithMockUser
    @DisplayName("예약게시글 생성 성공시 200 OK 와 응답 메시지를 반환한다.")
    void testCreateReservationArticle() throws Exception {
        // Given

        member = createTestMember();
        ReservationArticle reservationArticle = createTestReservationArticle(1L);

        CreateReservationArticleRequest createReservationArticleRequest =
                new CreateReservationArticleRequest(member.getId(), reservationArticle.getTitle(), reservationArticle.getContent(),
                reservationArticle.getHashTagList().stream().map(HashTag::getContent).toList(), List.of(LocalDateTime.now(),LocalDateTime.now().plusDays(2)));

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
}