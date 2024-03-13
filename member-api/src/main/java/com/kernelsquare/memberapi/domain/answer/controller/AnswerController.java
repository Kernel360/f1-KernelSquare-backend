package com.kernelsquare.memberapi.domain.answer.controller;

import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;
import com.kernelsquare.memberapi.domain.answer.dto.AnswerDto;
import com.kernelsquare.memberapi.domain.answer.dto.FindAllAnswerResponse;
import com.kernelsquare.memberapi.domain.answer.dto.UpdateAnswerRequest;
import com.kernelsquare.memberapi.domain.answer.facade.AnswerFacade;
import com.kernelsquare.memberapi.domain.answer.service.AnswerService;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.memberapi.domain.chatgpt.service.ChatGptService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.kernelsquare.core.common_response.response.code.AnswerResponseCode.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AnswerController {

	private final AnswerService answerService;
	private final AnswerFacade answerFacade;
	private final ChatGptService chatGptService;

	@GetMapping("/questions/{questionId}/answers")
	public ResponseEntity<ApiResponse<FindAllAnswerResponse>> findAllAnswer(
		@PathVariable("questionId") Long questionId) {
		FindAllAnswerResponse findAllAnswerResponse = answerService.findAllAnswer(questionId);
		return ResponseEntityFactory.toResponseEntity(ANSWERS_ALL_FOUND, findAllAnswerResponse);
	}

	@PostMapping("/questions/{questionId}/answers")
	public ResponseEntity<ApiResponse> createAnswer(
		@Valid @RequestBody AnswerDto.CreateRequest request,
		@PathVariable Long questionId,
		@AuthenticationPrincipal MemberAdapter memberAdapter
	) {
		answerFacade.createAnswer(request, questionId, memberAdapter);

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
		@PathVariable Long answerId,
		@AuthenticationPrincipal MemberAdapter memberAdapter
	) {
		answerService.updateAnswer(updateAnswerRequest, answerId, memberAdapter);
		return ResponseEntityFactory.toResponseEntity(ANSWER_UPDATED);
	}

	@DeleteMapping("/questions/answers/{answerId}")
	public ResponseEntity<ApiResponse> deleteAnswer(
		@PathVariable Long answerId,
		@AuthenticationPrincipal MemberAdapter memberAdapter
	) {
		answerService.deleteAnswer(answerId, memberAdapter);
		return ResponseEntityFactory.toResponseEntity(ANSWER_DELETED);
	}
}

