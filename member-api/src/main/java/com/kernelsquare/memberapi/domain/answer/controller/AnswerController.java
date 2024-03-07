package com.kernelsquare.memberapi.domain.answer.controller;

import static com.kernelsquare.core.common_response.response.code.AnswerResponseCode.*;

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
import com.kernelsquare.memberapi.domain.answer.dto.CreateAnswerRequest;
import com.kernelsquare.memberapi.domain.answer.dto.FindAllAnswerResponse;
import com.kernelsquare.memberapi.domain.answer.dto.UpdateAnswerRequest;
import com.kernelsquare.memberapi.domain.answer.service.AnswerService;
import com.kernelsquare.memberapi.domain.chatgpt.service.ChatGptService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AnswerController {

	private final AnswerService answerService;
	private final ChatGptService chatGptService;

	@GetMapping("/questions/{questionId}/answers")
	public ResponseEntity<ApiResponse<FindAllAnswerResponse>> findAllAnswers(
		@PathVariable("questionId") Long questionId) {
		FindAllAnswerResponse findAllAnswerResponse = answerService.findAllAnswer(questionId);
		return ResponseEntityFactory.toResponseEntity(ANSWERS_ALL_FOUND, findAllAnswerResponse);
	}

	@PostMapping("/questions/{questionId}/answers")
	public ResponseEntity<ApiResponse> createAnswer(
		@Valid @RequestBody CreateAnswerRequest createAnswerRequest,
		@PathVariable Long questionId
	) {
		answerService.createAnswer(createAnswerRequest, questionId);
		return ResponseEntityFactory.toResponseEntity(ANSWER_CREATED);
	}

	@PostMapping("/questions/{questionId}/answer-bot")
	public ResponseEntity<ApiResponse> createAnswerWithChatGpt(
		@PathVariable Long questionId
	) {
		chatGptService.createChatGptAnswer(questionId);
		return ResponseEntityFactory.toResponseEntity(AUTOMATED_ANSWER_CREATED);
	}

	@PutMapping("/questions/answers/{answerId}")
	public ResponseEntity<ApiResponse> updateAnswer(
		@Valid @RequestBody UpdateAnswerRequest updateAnswerRequest,
		@PathVariable Long answerId
	) {
		answerService.updateAnswer(updateAnswerRequest, answerId);
		return ResponseEntityFactory.toResponseEntity(ANSWER_UPDATED);
	}

	@DeleteMapping("/questions/answers/{answerId}")
	public ResponseEntity<ApiResponse> deleteAnswer(
		@PathVariable Long answerId
	) {
		answerService.deleteAnswer(answerId);
		return ResponseEntityFactory.toResponseEntity(ANSWER_DELETED);
	}
}

