package com.kernelsquare.adminapi.domain.answer.controller;

import static com.kernelsquare.core.common_response.response.code.AnswerResponseCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kernelsquare.adminapi.domain.answer.dto.FindAllAnswerResponse;
import com.kernelsquare.adminapi.domain.answer.service.AnswerService;
import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AnswerController {

	private final AnswerService answerService;

	@GetMapping("/questions/{questionId}/answers")
	public ResponseEntity<ApiResponse<FindAllAnswerResponse>> findAllAnswers(@PathVariable Long questionId) {
		FindAllAnswerResponse findAllAnswerResponse = answerService.findAllAnswer(questionId);
		return ResponseEntityFactory.toResponseEntity(ANSWERS_ALL_FOUND, findAllAnswerResponse);
	}

	@DeleteMapping("/questions/answers/{answerId}")
	public ResponseEntity<ApiResponse> deleteAnswer(
		@PathVariable Long answerId
	) {
		answerService.deleteAnswer(answerId);
		return ResponseEntityFactory.toResponseEntity(ANSWER_DELETED);
	}
}

