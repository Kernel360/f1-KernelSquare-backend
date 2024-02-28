package com.kernelsquare.adminapi.domain.auth.dto;

import com.kernelsquare.core.validation.ValidationGroups;
import com.kernelsquare.core.validation.constants.AuthValidationMessage;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record LoginRequest(
	@NotBlank(message = AuthValidationMessage.EMAIL_NOT_BLANK, groups = ValidationGroups.NotBlankGroup.class)
	@Size(min = 5, max = 40, message = AuthValidationMessage.EMAIL_SIZE, groups = ValidationGroups.SizeGroup.class)
	@Pattern(regexp = "^[^ㄱ-ㅎㅏ-ㅣ가-힣]*$", message = AuthValidationMessage.EMAIL_PATTERN, groups = ValidationGroups.PatternGroup.class)
	@Email(message = AuthValidationMessage.EMAIL, groups = ValidationGroups.EmailGroup.class)
	String email,

	@NotBlank(message = AuthValidationMessage.PASSWORD_NOT_BLANK, groups = ValidationGroups.NotBlankGroup.class)
	String password
) {
}
