package com.kernelsquare.adminapi.domain.reservation_article.controller;

import static com.kernelsquare.core.common_response.response.code.ReservationArticleResponseCode.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kernelsquare.adminapi.domain.reservation_article.dto.FindAllReservationArticleResponse;
import com.kernelsquare.adminapi.domain.reservation_article.dto.FindReservationArticleResponse;
import com.kernelsquare.adminapi.domain.reservation_article.service.ReservationArticleService;
import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;
import com.kernelsquare.core.dto.PageResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReservationArticleController {
	private final ReservationArticleService reservationArticleService;

	@GetMapping("/coffeechat/posts")
	public ResponseEntity<ApiResponse<PageResponse<FindAllReservationArticleResponse>>> findAllReservationArticle(
		@PageableDefault(page = 0, size = 5, sort = "createdDate", direction = Sort.Direction.DESC)
		Pageable pageable
	) {
		PageResponse<FindAllReservationArticleResponse> pageResponse = reservationArticleService.findAllReservationArticle(
			pageable);

		return ResponseEntityFactory.toResponseEntity(RESERVATION_ARTICLE_ALL_FOUND, pageResponse);
	}

	@GetMapping("/coffeechat/posts/{postId}")
	public ResponseEntity<ApiResponse<FindReservationArticleResponse>> findReservationArticle(
		@PathVariable
		Long postId
	) {
		FindReservationArticleResponse findReservationArticleResponse = reservationArticleService.findReservationArticle(
			postId);
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
