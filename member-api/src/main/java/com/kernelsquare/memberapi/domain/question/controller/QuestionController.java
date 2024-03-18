package com.kernelsquare.memberapi.domain.question.controller;

import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;
import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.memberapi.domain.question.dto.*;
import com.kernelsquare.memberapi.domain.question.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.kernelsquare.core.common_response.response.code.QuestionResponseCode.*;

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
	public ResponseEntity<ApiResponse<PageResponse<QuestionDto.FindAllResponse>>> findAllQuestions(
		@PageableDefault(page = 0, size = 10, sort = "createdDate", direction = Sort.Direction.DESC)
		Pageable pageable
	) {
		PageResponse<QuestionDto.FindAllResponse> pageResponse = questionService.findAllQuestions(pageable);

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
