package com.kernelsquare.adminapi.domain.search.controller;

import static com.kernelsquare.core.common_response.response.code.SearchResponseCode.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kernelsquare.adminapi.domain.search.dto.SearchQuestionResponse;
import com.kernelsquare.adminapi.domain.search.service.SearchService;
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
		@PageableDefault(page = 0, size = 5)
		Pageable pageable,
		@RequestParam
		String keyword
	) {
		SearchQuestionResponse searchResults = searchService.searchQuestions(pageable, keyword);
		return ResponseEntityFactory.toResponseEntity(SEARCH_QUESTION_COMPLETED, searchResults);
	}
}
