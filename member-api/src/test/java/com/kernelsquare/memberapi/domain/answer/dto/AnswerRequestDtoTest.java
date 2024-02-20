package com.kernelsquare.memberapi.domain.answer.dto;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@DisplayName("Answer 도메인 요청 Dto 종합 테스트")
class AnswerRequestDtoTest {
	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	Validator validator = factory.getValidator();

	@Test
	@DisplayName("답변 생성 요청 검증 테스트 - NotNull, NotBlank")
	void whenCreateAnswerIsNotBlank_thenValidationFails() {
		CreateAnswerRequest createAnswerRequest = CreateAnswerRequest.builder()
			.content("")
			.build();

		Set<ConstraintViolation<CreateAnswerRequest>> violations = validator.validate(createAnswerRequest);
		Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

		//then
		assertThat(msgList).isEqualTo(Set.of("회원 ID를 입력해 주세요.", "답변 내용을 입력해 주세요."));
	}

	@Test
	@DisplayName("답변 생성 요청 검증 성공 테스트 - Size")
	void whenCreateAnswerSizeExceedsLimit_thenValidationSucceeds() {
		CreateAnswerRequest createAnswerRequest = CreateAnswerRequest.builder()
				.memberId(1L)
				.content("a".repeat(10000))
				.build();

		Set<ConstraintViolation<CreateAnswerRequest>> violations = validator.validate(createAnswerRequest);
		Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

		assertThat(msgList).isEqualTo(Set.of());
	}

	@Test
	@DisplayName("답변 생성 요청 검증 실패 테스트 - Size")
	void whenCreateAnswerSizeExceedsLimit_thenValidationFails() {
		CreateAnswerRequest createAnswerRequest = CreateAnswerRequest.builder()
				.memberId(1L)
				.content("a".repeat(10001))
				.build();

		Set<ConstraintViolation<CreateAnswerRequest>> violations = validator.validate(createAnswerRequest);
		Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

		assertThat(msgList).isEqualTo(Set.of("답변 내용은 10000자를 넘을 수 없습니다."));
	}

	@Test
	@DisplayName("답변 수정 요청 검증 테스트 - NotNull, NotBlank")
	void whenUpdateAnswerIsNotBlank_thenValidationFails() {
		UpdateAnswerRequest updateAnswerRequest = UpdateAnswerRequest.builder()
			.content("")
			.build();

		Set<ConstraintViolation<UpdateAnswerRequest>> violations = validator.validate(updateAnswerRequest);
		Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

		//then
		assertThat(msgList).isEqualTo(Set.of("답변 내용을 입력해 주세요."));
	}

	@Test
	@DisplayName("답변 수정 요청 검증 성공 테스트 - Size")
	void whenUpdateAnswerSizeExceedsLimit_thenValidationSucceeds() {
		UpdateAnswerRequest updateAnswerRequest = UpdateAnswerRequest.builder()
				.content("a".repeat(10000))
				.build();

		Set<ConstraintViolation<UpdateAnswerRequest>> violations = validator.validate(updateAnswerRequest);
		Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

		assertThat(msgList).isEqualTo(Set.of());
	}

	@Test
	@DisplayName("답변 수정 요청 검증 실패 테스트 - Size")
	void whenUpdateAnswerSizeExceedsLimit_thenValidationFails() {
		UpdateAnswerRequest updateAnswerRequest = UpdateAnswerRequest.builder()
				.content("a".repeat(10001))
				.build();

		Set<ConstraintViolation<UpdateAnswerRequest>> violations = validator.validate(updateAnswerRequest);
		Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

		assertThat(msgList).isEqualTo(Set.of("답변 내용은 10000자를 넘을 수 없습니다."));
	}

}
