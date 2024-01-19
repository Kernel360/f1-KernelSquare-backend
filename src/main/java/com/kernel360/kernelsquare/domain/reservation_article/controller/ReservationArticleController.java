package com.kernel360.kernelsquare.domain.reservation_article.controller;

import com.kernel360.kernelsquare.domain.reservation_article.dto.CreateReservationArticleRequest;
import com.kernel360.kernelsquare.domain.reservation_article.dto.CreateReservationArticleResponse;
import com.kernel360.kernelsquare.domain.reservation_article.dto.FindAllReservationArticleResponse;
import com.kernel360.kernelsquare.domain.reservation_article.dto.FindReservationArticleResponse;
import com.kernel360.kernelsquare.domain.reservation_article.service.ReservationArticleService;
import com.kernel360.kernelsquare.global.common_response.ApiResponse;
import com.kernel360.kernelsquare.global.common_response.ResponseEntityFactory;
import com.kernel360.kernelsquare.global.dto.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.kernel360.kernelsquare.global.common_response.response.code.ReservationArticleResponseCode.*;

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

    @GetMapping("/coffeechat/posts")
    public ResponseEntity<ApiResponse<PageResponse<FindAllReservationArticleResponse>>> findAllReservationArticle (
        @PageableDefault(page = 0, size = 5, sort = "createdDate", direction = Sort.Direction.DESC)
        Pageable pageable
    ) {
        PageResponse<FindAllReservationArticleResponse> pageResponse = reservationArticleService.findAllReservationArticle(pageable);

        return ResponseEntityFactory.toResponseEntity(RESERVATION_ARTICLE_ALL_FOUND, pageResponse);
    }

    @GetMapping("/coffeechat/posts/{postId}")
    public ResponseEntity<ApiResponse<FindReservationArticleResponse>> findReservationArticle(
        @PathVariable
        Long postId
    ) {
        FindReservationArticleResponse findReservationArticleResponse = reservationArticleService.findReservationArticle(postId);
        return ResponseEntityFactory.toResponseEntity(RESERVATION_ARTICLE_FOUND, findReservationArticleResponse);
    }

    @DeleteMapping("coffeechat/posts/{postId}")
    public ResponseEntity<ApiResponse> deleteReservationArticle(
            @PathVariable
            Long postId
    ) {
        reservationArticleService.deleteReservationArticle(postId);

        return ResponseEntityFactory.toResponseEntity(RESERVATION_ARTICLE_DELETED);
    }

}
