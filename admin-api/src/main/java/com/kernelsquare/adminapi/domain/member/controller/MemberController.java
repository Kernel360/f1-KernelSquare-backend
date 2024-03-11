package com.kernelsquare.adminapi.domain.member.controller;

import com.kernelsquare.adminapi.domain.member.dto.FindMemberResponse;
import com.kernelsquare.adminapi.domain.member.dto.MemberDto;
import com.kernelsquare.adminapi.domain.member.facade.MemberFacade;
import com.kernelsquare.adminapi.domain.member.service.MemberService;
import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.kernelsquare.core.common_response.response.code.MemberResponseCode.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;
	private final MemberFacade memberFacade;

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

	@PutMapping("/members/role")
	public ResponseEntity<ApiResponse> updateMemberAuthority(
		@RequestBody
		MemberDto.UpdateAuthorityRequest request
	) {
		memberFacade.updateMemberAuthority(request);

		return ResponseEntityFactory.toResponseEntity(MEMBER_AUTHORITY_UPDATED);
	}
}
