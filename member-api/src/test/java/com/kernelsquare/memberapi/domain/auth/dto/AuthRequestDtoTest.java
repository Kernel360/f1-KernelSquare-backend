package com.kernelsquare.memberapi.domain.auth.dto;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.kernelsquare.core.validation.ValidationSequence;
import com.kernelsquare.core.validation.constants.AuthValidationMessage;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@DisplayName("Auth 도메인 요청 Dto 종합 테스트")
class AuthRequestDtoTest {
	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private final Validator validator = factory.getValidator();

	@Test
	@DisplayName("유효한 회원가입 요청에 대한 테스트")
	void validSignUpRequest() {
		// given
		SignUpRequest signUpRequest = SignUpRequest.builder()
			.email("valid@example.com")
			.nickname("validNi")
			.password("lid@Pad123")
			.build();

		// when
		Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(signUpRequest,
			ValidationSequence.class);

		// then
		assertThat(violations).isEmpty();
	}

	@Test
	@DisplayName("닉네임이 유효하지 않은 경우의 테스트")
	void invalidNickname() {
		// given
		SignUpRequest signUpRequest = SignUpRequest.builder()
			.email("valid@example.com")
			.nickname("invalidNickname1")
			.password("Valid@Password123")
			.build();

		// when
		Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(signUpRequest,
			ValidationSequence.class);

		// then
		assertThat(violations).hasSize(2);
		assertThat(violations).extracting("message").contains(AuthValidationMessage.NICKNAME_SIZE);
	}

	@Test
	@DisplayName("비밀번호가 유효하지 않은 경우의 테스트")
	void invalidPassword() {
		// given
		SignUpRequest signUpRequest = SignUpRequest.builder()
			.email("valid@example.com")
			.nickname("validNickname")
			.password("invalidpassword")
			.build();

		// when
		Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(signUpRequest,
			ValidationSequence.class);

		// then
		assertThat(violations).hasSize(1);
		assertThat(violations).extracting("message").contains(AuthValidationMessage.NICKNAME_SIZE);
	}

	@Test
	@DisplayName("이메일 주소가 빈 문자열일 때의 테스트")
	void blankEmail() {
		// given
		CheckDuplicateEmailRequest request = CheckDuplicateEmailRequest.builder()
			.email("")
			.build();

		// when
		Set<ConstraintViolation<CheckDuplicateEmailRequest>> violations = validator.validate(request,
			ValidationSequence.class);

		// then
		assertThat(violations).hasSize(1);
		assertThat(violations).extracting("message").contains(AuthValidationMessage.EMAIL_NOT_BLANK);
	}

	@Test
	@DisplayName("이메일 주소가 유효하지 않은 경우의 테스트")
	void invalidEmail() {
		// given
		CheckDuplicateEmailRequest request = CheckDuplicateEmailRequest.builder()
			.email("invalidEmail")
			.build();

		// when
		Set<ConstraintViolation<CheckDuplicateEmailRequest>> violations = validator.validate(request,
			ValidationSequence.class);

		// then
		assertThat(violations).hasSize(1);
		assertThat(violations).extracting("message").contains(AuthValidationMessage.EMAIL);
	}

	@Test
	@DisplayName("유효한 닉네임에 대한 테스트")
	void validNickname() {
		// given
		CheckDuplicateNicknameRequest request = CheckDuplicateNicknameRequest.builder()
			.nickname("validNic")
			.build();

		// when
		Set<ConstraintViolation<CheckDuplicateNicknameRequest>> violations = validator.validate(request,
			ValidationSequence.class);

		// then
		assertThat(violations).isEmpty();
	}

	@Test
	@DisplayName("유효한 로그인 요청에 대한 테스트")
	void validLoginRequest() {
		// given
		LoginRequest loginRequest = LoginRequest.builder()
			.email("valid@example.com")
			.password("ValidPassword123")
			.build();

		// when
		Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest, ValidationSequence.class);

		// then
		assertThat(violations).isEmpty();
	}
}