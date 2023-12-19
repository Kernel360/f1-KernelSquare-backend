package com.kernel360.kernelsquare.domain.member.controller;

import org.springframework.http.HttpStatus;
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
import com.kernel360.kernelsquare.global.dto.CommonApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;

	@PutMapping("/v1/members/{memberId}")
	public ResponseEntity<CommonApiResponse> update(@PathVariable Long memberId, @RequestBody
	UpdateMemberRequest updateMemberRequest) {
		memberService.updateMember(memberId, updateMemberRequest);
		return ResponseEntity.ok(CommonApiResponse.of(HttpStatus.OK, "회원 정보 수정 완료", null));
	}

	@PutMapping("/v1/members/{memberId}/password")
	public ResponseEntity<CommonApiResponse> updatePassword(@PathVariable Long memberId, @RequestBody String password) {
		memberService.updateMemberPassword(memberId, password);
		return ResponseEntity.ok(CommonApiResponse.of(HttpStatus.OK, "비밀번호 수정 완료", null));
	}

	@GetMapping("/v1/members/{memberId}")
	public ResponseEntity<CommonApiResponse<FindMemberResponse>> find(@PathVariable Long memberId) {
		FindMemberResponse findMemberResponse = memberService.findMember(memberId);
		return ResponseEntity.ok(CommonApiResponse.of(HttpStatus.OK, "회원 정보 조회 성공", findMemberResponse));
	}

	@DeleteMapping("/v1/members/{memberId}")
	public ResponseEntity<CommonApiResponse> delete(@PathVariable Long memberId) {
		return ResponseEntity.ok(CommonApiResponse.of(HttpStatus.OK, "회원 탈퇴 성공", null));
	}

}
