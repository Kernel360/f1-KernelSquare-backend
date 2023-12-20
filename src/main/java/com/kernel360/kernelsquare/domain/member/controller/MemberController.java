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

// todo : data가 존재하지 않는 응답일 때 data에 어떤 것을 넣을지? 혹은 어떻게 리팩토링을 통해서 data가 없는 응답을 만들어낼지..

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;

	@PutMapping("/members/{memberId}")
	public ResponseEntity<ApiResponse> updateMember(@PathVariable Long memberId,
		@RequestBody UpdateMemberRequest updateMemberRequest) {
		memberService.updateMember(memberId, updateMemberRequest);
		return ResponseEntityFactory.of(MEMBER_INFO_UPDATED);
	}

	@PutMapping("/members/{memberId}/password")
	public ResponseEntity<ApiResponse> updateMemberPassword(@PathVariable Long memberId,
		@RequestBody String password) {
		memberService.updateMemberPassword(memberId, password);
		ResponseEntityFactory.of(MEMBER_PASSWORD_UPDATED);
		return ResponseEntity.ok(ApiResponse.of(MEMBER_PASSWORD_UPDATED));
	}

	@GetMapping("/members/{memberId}")
	public ResponseEntity<ApiResponse<FindMemberResponse>> findMember(@PathVariable Long memberId) {
		FindMemberResponse findMemberResponse = memberService.findMember(memberId);
		return ResponseEntity.ok(ApiResponse.of(MEMBER_FOUND, findMemberResponse));
	}

	@DeleteMapping("/members/{memberId}")
	public ResponseEntity<ApiResponse> deleteMember(@PathVariable Long memberId) {
		memberService.deleteMember(memberId);
		return ResponseEntity.ok(ApiResponse.of(MEMBER_DELETED));
	}
}
