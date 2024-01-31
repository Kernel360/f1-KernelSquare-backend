package com.kernelsquare.adminapi.domain.question.controller;

import static com.kernelsquare.core.common_response.response.code.QuestionResponseCode.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kernelsquare.adminapi.domain.question.dto.FindQuestionResponse;
import com.kernelsquare.adminapi.domain.question.service.QuestionService;
import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;
import com.kernelsquare.core.dto.PageResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class QuestionController {
	private final QuestionService questionService;

	@GetMapping("/questions/{questionId}")
	public ResponseEntity<ApiResponse<FindQuestionResponse>> findQuestion(
		@PathVariable
		Long questionId
	) {
		FindQuestionResponse findQuestionResponse = questionService.findQuestion(questionId);

		return ResponseEntityFactory.toResponseEntity(QUESTION_FOUND, findQuestionResponse);
	}

	@GetMapping("questions")
	public ResponseEntity<ApiResponse<PageResponse<FindQuestionResponse>>> findAllQuestions(
		@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.DESC)
		Pageable pageable
	) {
		PageResponse<FindQuestionResponse> pageResponse = questionService.findAllQuestions(pageable);

		return ResponseEntityFactory.toResponseEntity(QUESTION_ALL_FOUND, pageResponse);
	}

	@DeleteMapping("/questions/{questionId}")
	public ResponseEntity<ApiResponse> deleteQuestion(
		@PathVariable
		Long questionId
	) {
		questionService.deleteQuestion(questionId);

		return ResponseEntityFactory.toResponseEntity(QUESTION_DELETED);
	}
}
