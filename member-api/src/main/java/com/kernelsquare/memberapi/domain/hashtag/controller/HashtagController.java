package com.kernelsquare.memberapi.domain.hashtag.controller;

import static com.kernelsquare.core.common_response.response.code.HashtagResponseCode.HashTagResponseCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;
import com.kernelsquare.memberapi.domain.hashtag.dto.FindAllHashtagResponse;
import com.kernelsquare.memberapi.domain.hashtag.service.HashtagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class HashtagController {
	private final HashtagService hashtagService;

	@GetMapping("/hashtags")
	public ResponseEntity<ApiResponse<FindAllHashtagResponse>> findAllHashtag() {
		FindAllHashtagResponse response = hashtagService.findAllHashtag();

		return ResponseEntityFactory.toResponseEntity(HASHTAG_ALL_FOUND, response);
	}

	@DeleteMapping("/hashtags/{hashtagId}")
	public ResponseEntity<ApiResponse> deleteHashtag(
		@PathVariable
		Long hashtagId
	) {
		hashtagService.deleteHashtag(hashtagId);

		return ResponseEntityFactory.toResponseEntity(HASHTAG_DELETED);
	}
}
