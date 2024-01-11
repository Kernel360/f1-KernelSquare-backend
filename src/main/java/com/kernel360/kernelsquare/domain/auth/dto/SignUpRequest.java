package com.kernel360.kernelsquare.domain.auth.dto;

import com.kernel360.kernelsquare.domain.level.entity.Level;
import com.kernel360.kernelsquare.domain.member.entity.Member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record SignUpRequest(
	@NotBlank(message = "이메일은 필수 입력 항목입니다.")
	@Email(message = "이메일 형식을 확인해 주세요")
	@Size(min = 5, max = 40, message = "이메일 길이를 확인해 주세요")
	String email,

	@NotBlank(message = "닉네임은 필수 입력 항목입니다.")
	@Size(min = 2, max = 8, message = "닉네임 길이를 확인해 주세요")
	String nickname,

	@NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
	@Size(min = 8, max = 16, message = "비밀번호 길이를 확인해 주세요")
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
			.introduction(null)
			.level(level)
			.build();
	}
}
