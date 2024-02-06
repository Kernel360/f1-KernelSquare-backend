package com.kernelsquare.memberapi.domain.auth.dto;

import com.kernelsquare.core.validation.ValidationGroups;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CheckDuplicateNicknameRequest(
	@NotBlank(message = "닉네임을 입력해 주세요.", groups = ValidationGroups.NotBlankGroup.class)
	@Size(min = 2, max = 8, message = "닉네임 길이를 확인해 주세요.", groups = ValidationGroups.SizeGroup.class)
	@Pattern(regexp = "^[가-힣a-zA-Z]+$", message = "완전한 한글 조합 또는 영문 대소문자만 입력하세요.", groups = ValidationGroups.PatternGroup.class)
	String nickname
) {
}
