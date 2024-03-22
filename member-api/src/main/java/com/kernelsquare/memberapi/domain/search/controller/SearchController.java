package com.kernelsquare.memberapi.domain.search.controller;

import static com.kernelsquare.core.common_response.response.code.SearchResponseCode.*;

import com.kernelsquare.memberapi.domain.search.dto.SearchCodingMeetingResponse;
import com.kernelsquare.memberapi.domain.search.dto.SearchReservationArticleResponse;
import com.kernelsquare.memberapi.domain.search.dto.SearchTechStackResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kernelsquare.memberapi.domain.search.dto.SearchQuestionResponse;
import com.kernelsquare.memberapi.domain.search.service.SearchService;
import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SearchController {
	private final SearchService searchService;

	@GetMapping("/search/questions")
	public ResponseEntity<ApiResponse<SearchQuestionResponse>> searchQuestions(
		@PageableDefault(page = 0, size = 10)
		Pageable pageable,
		@RequestParam
		String keyword
	) {
		SearchQuestionResponse searchResults = searchService.searchQuestions(pageable, keyword);

		return ResponseEntityFactory.toResponseEntity(SEARCH_QUESTION_COMPLETED, searchResults);
	}

	@GetMapping("/search/techs")
	public ResponseEntity<ApiResponse<SearchTechStackResponse>> searchTechStacks(
		@PageableDefault(page = 0, size = 10)
		Pageable pageable,
		@RequestParam
		String keyword
	) {
		SearchTechStackResponse searchResults = searchService.searchTechStacks(pageable, keyword);

		return ResponseEntityFactory.toResponseEntity(SEARCH_TECH_STACK_COMPLETED, searchResults);
	}

	@GetMapping("/search/coffeechat/posts")
	public ResponseEntity<ApiResponse<SearchReservationArticleResponse>> searchReservationArticles(
		@PageableDefault(page = 0, size = 5)
		Pageable pageable,
		@RequestParam
		String keyword
	) {
		SearchReservationArticleResponse searchResults = searchService.searchReservationArticles(pageable, keyword);

		return ResponseEntityFactory.toResponseEntity(SEARCH_RESERVATION_ARTICLE_COMPLETED, searchResults);
	}

	@GetMapping("/search/coding-meetings")
	public ResponseEntity<ApiResponse<SearchCodingMeetingResponse>> searchCodingMeetings(
		@PageableDefault(page = 0, size = 10)
		Pageable pageable,
		@RequestParam(defaultValue = "all")
		String filter,
		@RequestParam
		String keyword
	) {
		SearchCodingMeetingResponse searchResults = searchService.searchCodingMeetings(pageable, filter, keyword);

		return ResponseEntityFactory.toResponseEntity(SEARCH_CODING_MEETING_COMPLETED, searchResults);
	}
}

