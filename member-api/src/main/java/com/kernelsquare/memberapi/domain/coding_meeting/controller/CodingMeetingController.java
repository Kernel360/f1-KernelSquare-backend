package com.kernelsquare.memberapi.domain.coding_meeting.controller;

import static com.kernelsquare.core.common_response.response.code.CodingMeetingResponseCode.*;

import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;
import com.kernelsquare.core.constants.SpeLConstants;
import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.memberapi.domain.coding_meeting.dto.CodingMeetingDto;
import com.kernelsquare.memberapi.domain.coding_meeting.service.CodingMeetingFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CodingMeetingController {
    private final CodingMeetingFacade codingMeetingFacade;

    @PostMapping("/coding-meetings")
    public ResponseEntity<ApiResponse<CodingMeetingDto.CreateResponse>> createCodingMeeting(
        @Valid @RequestBody CodingMeetingDto.CreateRequest request,
        @AuthenticationPrincipal MemberAdapter memberAdapter) {
        CodingMeetingDto.CreateResponse findResponse = codingMeetingFacade.createCodingMeeting(request, memberAdapter.getMember().getId());
        return ResponseEntityFactory.toResponseEntity(CODING_MEETING_CREATED, findResponse);
    }

    @GetMapping("/coding-meetings/{codingMeetingToken}")
    public ResponseEntity<ApiResponse<CodingMeetingDto.FindResponse>> findCodingMeeting(
        @PathVariable String codingMeetingToken) {
        CodingMeetingDto.FindResponse findResponse = codingMeetingFacade.findCodingMeeting(codingMeetingToken);
        return ResponseEntityFactory.toResponseEntity(CODING_MEETING_FOUND, findResponse);
    }

    @GetMapping("/coding-meetings")
    public ResponseEntity<ApiResponse<PageResponse<CodingMeetingDto.FindAllResponse>>> findAllCodingMeeting(
        @PageableDefault(page = 0, size = 10, sort = "createdDate", direction = Sort.Direction.DESC)
        Pageable pageable,
        @RequestParam(defaultValue = "all")
        String filter,
        @AuthenticationPrincipal(expression = SpeLConstants.ANONYMOUS_NULL)
        MemberAdapter memberAdapter) {
        PageResponse pageResponse = codingMeetingFacade.findAllCodingMeeting(pageable, filter, memberAdapter);
        return ResponseEntityFactory.toResponseEntity(CODING_MEETING_ALL_FOUND, pageResponse);
    }

    @PutMapping("/coding-meetings/{codingMeetingToken}")
    public ResponseEntity<ApiResponse> updateCodingMeeting(
        @PathVariable String codingMeetingToken,
        @Valid @RequestBody CodingMeetingDto.UpdateRequest request) {
        codingMeetingFacade.updateCodingMeeting(request, codingMeetingToken);
        return ResponseEntityFactory.toResponseEntity(CODING_MEETING_UPDATED);
    }

    @DeleteMapping("/coding-meetings/{codingMeetingToken}")
    public ResponseEntity<ApiResponse> deleteCodingMeeting(
        @PathVariable String codingMeetingToken) {
        codingMeetingFacade.deleteCodingMeeting(codingMeetingToken);
        return ResponseEntityFactory.toResponseEntity(CODING_MEETING_DELETED);
    }

    @PutMapping("/coding-meetings/{codingMeetingToken}/status")
    public ResponseEntity<ApiResponse> closeCodingMeeting(
        @PathVariable String codingMeetingToken) {
        codingMeetingFacade.closeCodingMeeting(codingMeetingToken);
        return ResponseEntityFactory.toResponseEntity(CODING_MEETING_CLOSED);
    }
}
