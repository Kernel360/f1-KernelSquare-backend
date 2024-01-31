package com.kernelsquare.adminapi.domain.auth.dto;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@DisplayName("Auth 도메인 요청 Dto 종합 테스트")
class AuthRequestDtoTest {
	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	Validator validator = factory.getValidator();

	@Test
	@DisplayName("이메일 중복 체크 검증 테스트")
	void validateCheckDuplicateEmailRequest() {
		CheckDuplicateEmailRequest checkDuplicateEmailRequest1 = CheckDuplicateEmailRequest.builder()
			.email("")
			.build();

		CheckDuplicateEmailRequest checkDuplicateEmailRequest2 = CheckDuplicateEmailRequest.builder()
			.email("asdsaas")
			.build();

		Set<ConstraintViolation<CheckDuplicateEmailRequest>> violations1 = validator.validate(
			checkDuplicateEmailRequest1);
		Set<String> msgList1 = violations1.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

		Set<ConstraintViolation<CheckDuplicateEmailRequest>> violations2 = validator.validate(
			checkDuplicateEmailRequest2);
		Set<String> msgList2 = violations2.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

		//then
		assertThat(msgList1).isEqualTo(Set.of("이메일을 입력해 주세요.", "이메일 길이를 확인해 주세요."));
		assertThat(msgList2).isEqualTo(Set.of("이메일 형식으로 입력해 주세요."));
	}

	@Test
	@DisplayName("닉네임 중복 체크 검증 테스트")
	void validateCheckDuplicateNicknameRequest() {
		CheckDuplicateNicknameRequest checkDuplicateNicknameRequest = CheckDuplicateNicknameRequest.builder()
			.nickname("")
			.build();

		Set<ConstraintViolation<CheckDuplicateNicknameRequest>> violations = validator.validate(
			checkDuplicateNicknameRequest);
		Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

		//then
		assertThat(msgList).isEqualTo(Set.of("닉네임을 입력해 주세요.", "닉네임 길이를 확인해 주세요."));
	}

	@Test
	@DisplayName("로그인 요청 검증 테스트")
	void validateLoginRequest() {
		LoginRequest loginRequest1 = LoginRequest.builder()
			.email("")
			.password("")
			.build();

		LoginRequest loginRequest2 = LoginRequest.builder()
			.email("asdasdas")
			.password("asdassdd")
			.build();

		Set<ConstraintViolation<LoginRequest>> violations1 = validator.validate(loginRequest1);
		Set<String> msgList1 = violations1.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

		Set<ConstraintViolation<LoginRequest>> violations2 = validator.validate(loginRequest2);
		Set<String> msgList2 = violations2.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

		//then
		assertThat(msgList1).isEqualTo(Set.of("이메일을 입력해 주세요.", "이메일 길이를 확인해 주세요.",
			"비밀번호를 입력해 주세요.", "비밀번호 길이를 확인해 주세요."));

		assertThat(msgList2).isEqualTo(Set.of("이메일 형식으로 입력해 주세요."));
	}

	@Test
	@DisplayName("회원가입 요청 검증 테스트")
	void validateSignUpRequest() {
		SignUpRequest signUpRequest1 = SignUpRequest.builder()
			.email("")
			.nickname("")
			.password("")
			.build();

		SignUpRequest signUpRequest2 = SignUpRequest.builder()
			.email("asdasdas")
			.nickname("홍박사")
			.password("asdasdas")
			.build();

		Set<ConstraintViolation<SignUpRequest>> violations1 = validator.validate(signUpRequest1);
		Set<String> msgList1 = violations1.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

		Set<ConstraintViolation<SignUpRequest>> violations2 = validator.validate(signUpRequest2);
		Set<String> msgList2 = violations2.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

		//then
		assertThat(msgList1).isEqualTo(Set.of("이메일을 입력해 주세요.", "이메일 길이를 확인해 주세요.",
			"닉네임을 입력해 주세요.", "닉네임 길이를 확인해 주세요.",
			"비밀번호를 입력해 주세요.", "비밀번호 길이를 확인해 주세요."));

		assertThat(msgList2).isEqualTo(Set.of("이메일 형식으로 입력해 주세요."));
	}

}