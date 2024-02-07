package com.kernelsquare.memberapi.domain.auth.dto;

import com.kernelsquare.core.validation.ValidationGroups;
import com.kernelsquare.core.validation.constants.AuthValidationConstants;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CheckDuplicateEmailRequest(

	@NotBlank(message = AuthValidationConstants.EMAIL_NOT_BLANK, groups = ValidationGroups.NotBlankGroup.class)
	@Size(min = 5, max = 40, message = AuthValidationConstants.EMAIL_SIZE, groups = ValidationGroups.SizeGroup.class)
	@Pattern(regexp = "^[^ㄱ-ㅎㅏ-ㅣ가-힣]*$", message = AuthValidationConstants.EMAIL_PATTERN, groups = ValidationGroups.PatternGroup.class)
	@Email(message = AuthValidationConstants.EMAIL, groups = ValidationGroups.EmailGroup.class)
	String email
) {
}