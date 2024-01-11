package com.kernel360.kernelsquare.domain.reservation_article.controller;

import com.kernel360.kernelsquare.domain.reservation_article.dto.CreateReservationArticleRequest;
import com.kernel360.kernelsquare.domain.reservation_article.dto.CreateReservationArticleResponse;
import com.kernel360.kernelsquare.domain.reservation_article.service.ReservationArticleService;
import com.kernel360.kernelsquare.global.common_response.ApiResponse;
import com.kernel360.kernelsquare.global.common_response.ResponseEntityFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kernel360.kernelsquare.global.common_response.response.code.ReservationArticleResponseCode.RESERVATION_ARTICLE_CREATED;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReservationArticleController {

    private final ReservationArticleService reservationArticleService;
    @PostMapping("/coffeechat/posts")
    public ResponseEntity<ApiResponse<CreateReservationArticleResponse>> createReservationArticle(
            @Valid
            @RequestBody
            CreateReservationArticleRequest createReservationArticleRequest
    ) {
        CreateReservationArticleResponse createReservationArticleResponse = reservationArticleService.createReservationArticle(createReservationArticleRequest);

        return ResponseEntityFactory.toResponseEntity(RESERVATION_ARTICLE_CREATED, createReservationArticleResponse);
    }
}
