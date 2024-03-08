package com.kernelsquare.memberapi.domain.question.controller;

import static com.kernelsquare.core.common_response.response.code.QuestionResponseCode.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;
import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.memberapi.domain.question.dto.CreateQuestionRequest;
import com.kernelsquare.memberapi.domain.question.dto.CreateQuestionResponse;
import com.kernelsquare.memberapi.domain.question.dto.FindQuestionResponse;
import com.kernelsquare.memberapi.domain.question.dto.UpdateQuestionRequest;
import com.kernelsquare.memberapi.domain.question.service.QuestionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class QuestionController {
	private final QuestionService questionService;

	@PostMapping("/questions")
	public ResponseEntity<ApiResponse<CreateQuestionResponse>> createQuestion(
		@Valid
		@RequestBody
		CreateQuestionRequest createQuestionRequest
	) {
		CreateQuestionResponse createQuestionResponse = questionService.createQuestion(createQuestionRequest);

		return ResponseEntityFactory.toResponseEntity(QUESTION_CREATED, createQuestionResponse);
	}

	@GetMapping("/questions/{questionId}")
	public ResponseEntity<ApiResponse<FindQuestionResponse>> findQuestion(
		@PathVariable
		Long questionId
	) {
		FindQuestionResponse findQuestionResponse = questionService.findQuestion(questionId);

		return ResponseEntityFactory.toResponseEntity(QUESTION_FOUND, findQuestionResponse);
	}

	@GetMapping("/questions")
	public ResponseEntity<ApiResponse<PageResponse<FindQuestionResponse>>> findAllQuestions(
		@PageableDefault(page = 0, size = 10, sort = "createdDate", direction = Sort.Direction.DESC)
		Pageable pageable
	) {
		PageResponse<FindQuestionResponse> pageResponse = questionService.findAllQuestions(pageable);

		return ResponseEntityFactory.toResponseEntity(QUESTION_ALL_FOUND, pageResponse);
	}

	@PutMapping("/questions/{questionId}")
	public ResponseEntity<ApiResponse> updateQuestion(
		@PathVariable
		Long questionId,
		@Valid
		@RequestBody
		UpdateQuestionRequest updateQuestionRequest
	) {
		questionService.updateQuestion(questionId, updateQuestionRequest);

		return ResponseEntityFactory.toResponseEntity(QUESTION_UPDATED);
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
