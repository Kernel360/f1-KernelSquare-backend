package com.kernel360.kernelsquare.domain.member_answer_vote.controller;

import com.kernel360.kernelsquare.domain.member_answer_vote.dto.CreateMemberAnswerVoteRequest;
import com.kernel360.kernelsquare.domain.member_answer_vote.service.MemberAnswerVoteService;
import com.kernel360.kernelsquare.global.common_response.ApiResponse;
import com.kernel360.kernelsquare.global.common_response.ResponseEntityFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.kernel360.kernelsquare.global.common_response.response.code.MemberAnswerVoteResponseCode.MEMBER_ANSWER_VOTE_CREATED;
import static com.kernel360.kernelsquare.global.common_response.response.code.MemberAnswerVoteResponseCode.MEMBER_ANSWER_VOTE_DELETED;

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
