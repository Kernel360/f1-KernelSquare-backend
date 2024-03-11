package com.kernelsquare.adminapi.domain.member.controller;

import com.kernelsquare.adminapi.domain.member.dto.MemberDto;
import com.kernelsquare.adminapi.domain.member.facade.MemberFacade;
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
	private final MemberFacade memberFacade;

	@GetMapping("/members/{memberId}")
	public ResponseEntity<ApiResponse<MemberDto.FindResponse>> findMember(@PathVariable Long memberId) {
		MemberDto.FindResponse response = memberFacade.findMember(memberId);
		return ResponseEntityFactory.toResponseEntity(MEMBER_FOUND, response);
	}

	@DeleteMapping("/members/{memberId}")
	public ResponseEntity<ApiResponse> deleteMember(@PathVariable Long memberId) {
		memberFacade.deleteMember(memberId);
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

	@PutMapping("/members/nick")
	public ResponseEntity<ApiResponse<MemberDto.FindResponse>> updateMemberNickname(
		@RequestBody
		MemberDto.UpdateNicknameRequest request
	) {
		MemberDto.FindResponse response = memberFacade.updateMemberNickname(request);

		return ResponseEntityFactory.toResponseEntity(MEMBER_NICKNAME_UPDATED, response);
	}
}
