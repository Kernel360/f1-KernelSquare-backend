package com.kernelsquare.memberapi.domain.member_answer_vote.controller;

import static com.kernelsquare.core.common_response.response.code.MemberAnswerVoteResponseCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;
import com.kernelsquare.memberapi.domain.member_answer_vote.dto.CreateMemberAnswerVoteRequest;
import com.kernelsquare.memberapi.domain.member_answer_vote.service.MemberAnswerVoteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MemberAnswerVoteController {
	private final MemberAnswerVoteService memberAnswerVoteService;

	@PostMapping("/questions/answers/{answerId}/vote")
	public ResponseEntity<ApiResponse> createVote(
		@Valid @RequestBody CreateMemberAnswerVoteRequest createMemberAnswerVoteRequest,
		@PathVariable Long answerId
	) {
		memberAnswerVoteService.createVote(createMemberAnswerVoteRequest, answerId);
		return ResponseEntityFactory.toResponseEntity(MEMBER_ANSWER_VOTE_CREATED);
	}

	@DeleteMapping("/questions/answers/{answerId}/vote")
	public ResponseEntity<ApiResponse> deleteVote(
		@PathVariable Long answerId
	) {
		memberAnswerVoteService.deleteVote(answerId);
		return ResponseEntityFactory.toResponseEntity(MEMBER_ANSWER_VOTE_DELETED);
	}
}
