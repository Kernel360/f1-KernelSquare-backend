package com.kernel360.kernelsquare.domain.member.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kernel360.kernelsquare.domain.member.dto.FindMemberResponse;
import com.kernel360.kernelsquare.domain.member.dto.UpdateMemberRequest;
import com.kernel360.kernelsquare.domain.member.dto.UpdateMemberResponse;
import com.kernel360.kernelsquare.domain.member.service.MemberService;
import com.kernel360.kernelsquare.global.dto.CommonApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;

	@PutMapping("/members/{memberId}")
	public CommonApiResponse<UpdateMemberResponse> update(@PathVariable Long memberId, @RequestBody
	UpdateMemberRequest updateMemberRequest) {
		UpdateMemberResponse updateMemberResponse = memberService.update(memberId, updateMemberRequest);
		return CommonApiResponse.of("200", "회원 정보 수정 완료", updateMemberResponse);
	}

	@GetMapping("/members/{memberId}")
	public CommonApiResponse<FindMemberResponse> find(@PathVariable Long memberId) {
		FindMemberResponse findMemberResponse = memberService.find(memberId);
		return CommonApiResponse.of("200", "회원 정보 조회 성공", findMemberResponse);
	}

	@DeleteMapping("/members/{memberId}")
	public CommonApiResponse delete(@PathVariable Long memberId) {
		return CommonApiResponse.of("200", "회원 탈퇴 성공", null);
	}
}
