package com.kernelsquare.memberapi.domain.auth.dto;

import com.kernelsquare.core.validation.ValidationGroups;
import com.kernelsquare.core.validation.constants.AuthValidationConstants;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record SignUpRequest(
	@NotBlank(message = AuthValidationConstants.EMAIL_NOT_BLANK, groups = ValidationGroups.NotBlankGroup.class)
	@Size(min = 5, max = 40, message = AuthValidationConstants.EMAIL_SIZE, groups = ValidationGroups.SizeGroup.class)
	@Pattern(regexp = "^[^ㄱ-ㅎㅏ-ㅣ가-힣]*$", message = AuthValidationConstants.EMAIL_PATTERN, groups = ValidationGroups.PatternGroup.class)
	@Email(message = AuthValidationConstants.EMAIL, groups = ValidationGroups.EmailGroup.class)
	String email,

	@NotBlank(message = AuthValidationConstants.NICKNAME_NOT_BLANK, groups = ValidationGroups.NotBlankGroup.class)
	@Size(min = 2, max = 8, message = AuthValidationConstants.NICKNAME_SIZE, groups = ValidationGroups.SizeGroup.class)
	@Pattern(regexp = "^[가-힣a-zA-Z]+$", message = AuthValidationConstants.NICKNAME_PATTERN, groups = ValidationGroups.PatternGroup.class)
	String nickname,

	@NotBlank(message = AuthValidationConstants.PASSWORD_NOT_BLANK, groups = ValidationGroups.NotBlankGroup.class)
	@Size(min = 8, max = 16, message = AuthValidationConstants.PASSWORD_SIZE, groups = ValidationGroups.SizeGroup.class)
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
		message = AuthValidationConstants.PASSWORD_PATTERN, groups = ValidationGroups.PatternGroup.class)
	String password
) {

	public static Member toEntity(SignUpRequest signUpRequest, String password, Level
		level) {
		return Member.builder()
			.email(signUpRequest.email())
			.nickname(signUpRequest.nickname())
			.password(password)
			.experience(0L)
			.imageUrl(null)
			.introduction("")
			.level(level)
			.build();
	}
}