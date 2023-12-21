package com.kernel360.kernelsquare.domain.member.controller;

import static com.kernel360.kernelsquare.global.common_response.response.code.MemberResponseCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kernel360.kernelsquare.domain.member.dto.FindMemberResponse;
import com.kernel360.kernelsquare.domain.member.dto.UpdateMemberRequest;
import com.kernel360.kernelsquare.domain.member.service.MemberService;
import com.kernel360.kernelsquare.global.common_response.ApiResponse;
import com.kernel360.kernelsquare.global.common_response.ResponseEntityFactory;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;

	@PutMapping("/members/{memberId}")
	public ResponseEntity<ApiResponse> updateMember(@PathVariable Long memberId,
		@RequestBody UpdateMemberRequest updateMemberRequest) {
		memberService.updateMember(memberId, updateMemberRequest);
		return ResponseEntityFactory.toResponseEntity(MEMBER_INFO_UPDATED);
	}

	@PutMapping("/members/{memberId}/password")
	public ResponseEntity<ApiResponse> updateMemberPassword(@PathVariable Long memberId,
		@RequestBody String password) {
		memberService.updateMemberPassword(memberId, password);
		return ResponseEntityFactory.toResponseEntity(MEMBER_PASSWORD_UPDATED);
	}

	@GetMapping("/members/{memberId}")
	public ResponseEntity<ApiResponse<FindMemberResponse>> findMember(@PathVariable Long memberId) {
		FindMemberResponse findMemberResponse = memberService.findMember(memberId);
		return ResponseEntityFactory.toResponseEntity(MEMBER_FOUND, findMemberResponse);
	}

	@DeleteMapping("/members/{memberId}")
	public ResponseEntity<ApiResponse> deleteMember(@PathVariable Long memberId) {
		memberService.deleteMember(memberId);
		return ResponseEntityFactory.toResponseEntity(MEMBER_DELETED);
	}
}
