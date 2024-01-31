package com.kernelsquare.adminapi.domain.member.controller;

import static com.kernelsquare.core.common_response.response.code.MemberResponseCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kernelsquare.adminapi.domain.member.dto.FindMemberResponse;
import com.kernelsquare.adminapi.domain.member.service.MemberService;
import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;

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
