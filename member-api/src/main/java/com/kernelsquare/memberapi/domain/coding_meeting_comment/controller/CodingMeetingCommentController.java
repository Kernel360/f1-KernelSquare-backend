package com.kernelsquare.memberapi.domain.coding_meeting_comment.controller;

import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.memberapi.domain.coding_meeting_comment.dto.CodingMeetingCommentDto;
import com.kernelsquare.memberapi.domain.coding_meeting_comment.service.CodingMeetingCommentFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.kernelsquare.core.common_response.response.code.CodingMeetingCommentResponseCode.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CodingMeetingCommentController {
    private final CodingMeetingCommentFacade codingMeetingCommentFacade;

    @PostMapping("coding-meeting-comments")
    public ResponseEntity<ApiResponse> createCodingMeetingComment(
        @Valid @RequestBody CodingMeetingCommentDto.CreateRequest request,
        @AuthenticationPrincipal MemberAdapter memberAdapter) {
        codingMeetingCommentFacade.createCodingMeetingComment(request, memberAdapter.getMember().getId());
        return ResponseEntityFactory.toResponseEntity(CODING_MEETING_COMMENT_CREATED);
    }

    @GetMapping("coding-meeting-comments/{codingMeetingToken}")
    public ResponseEntity<ApiResponse<List<CodingMeetingCommentDto.FindAllResponse>>> findAllCodingMeetingComment(
        @PathVariable String codingMeetingToken) {
        List<CodingMeetingCommentDto.FindAllResponse> findAllResponse = codingMeetingCommentFacade.findAllCodingMeetingComment(codingMeetingToken);
        return ResponseEntityFactory.toResponseEntity(CODING_MEETING_COMMENT_ALL_FOUND, findAllResponse);
    }

    @PutMapping("coding-meeting-comments/{codingMeetingCommentToken}")
    public ResponseEntity<ApiResponse> updateCodingMeetingComment(
        @PathVariable String codingMeetingCommentToken,
        @Valid @RequestBody CodingMeetingCommentDto.UpdateRequest request) {
        codingMeetingCommentFacade.updateCodingMeetingComment(request, codingMeetingCommentToken);
        return ResponseEntityFactory.toResponseEntity(CODING_MEETING_COMMENT_UPDATED);
    }

    @DeleteMapping("coding-meeting-comments/{codingMeetingCommentToken}")
    public ResponseEntity<ApiResponse> deleteCodingMeetingComment(
        @PathVariable String codingMeetingCommentToken) {
        codingMeetingCommentFacade.deleteCodingMeetingComment(codingMeetingCommentToken);
        return ResponseEntityFactory.toResponseEntity(CODING_MEETING_COMMENT_DELETED);
    }

}
