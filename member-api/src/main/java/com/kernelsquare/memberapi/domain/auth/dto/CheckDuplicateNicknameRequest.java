package com.kernelsquare.memberapi.domain.auth.dto;

import com.kernelsquare.core.validation.ValidationGroups;
import com.kernelsquare.core.validation.constants.AuthValidationConstants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CheckDuplicateNicknameRequest(
	@NotBlank(message = AuthValidationConstants.NICKNAME_NOT_BLANK, groups = ValidationGroups.NotBlankGroup.class)
	@Size(min = 2, max = 8, message = AuthValidationConstants.NICKNAME_SIZE, groups = ValidationGroups.SizeGroup.class)
	@Pattern(regexp = "^[가-힣a-zA-Z]+$", message = AuthValidationConstants.NICKNAME_PATTERN, groups = ValidationGroups.PatternGroup.class)
	String nickname
) {
}