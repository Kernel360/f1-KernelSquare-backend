package com.kernel360.kernelsquare.domain.reservation_article.controller;

import com.kernel360.kernelsquare.domain.reservation_article.dto.CreateReservationArticleRequest;
import com.kernel360.kernelsquare.domain.reservation_article.dto.CreateReservationArticleResponse;
import com.kernel360.kernelsquare.domain.reservation_article.dto.FindReservationArticleResponse;
import com.kernel360.kernelsquare.domain.reservation_article.service.ReservationArticleService;
import com.kernel360.kernelsquare.global.common_response.ApiResponse;
import com.kernel360.kernelsquare.global.common_response.ResponseEntityFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.kernel360.kernelsquare.global.common_response.response.code.ReservationArticleResponseCode.RESERVATION_ARTICLE_CREATED;
import static com.kernel360.kernelsquare.global.common_response.response.code.ReservationArticleResponseCode.RESERVATION_ARTICLE_FOUND;

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

    // TODO 질문 목록 조회

    // TODO 질문 단건 조회
    @GetMapping("/coffeechat/posts/{postId}")
    public ResponseEntity<ApiResponse<FindReservationArticleResponse>> findReservationArticle(
        @PathVariable
        Long postId
    ) {
        FindReservationArticleResponse findReservationArticleResponse = reservationArticleService.findReservationArticle(postId);
        return ResponseEntityFactory.toResponseEntity(RESERVATION_ARTICLE_FOUND, findReservationArticleResponse);
    }

}
