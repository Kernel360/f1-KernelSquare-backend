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
	@DisplayName("답변 생성 요청 검증 테스트")
	void validateCreateAnswerRequest() {
		CreateAnswerRequest createAnswerRequest = CreateAnswerRequest.builder()
			.content("")
			.build();

		Set<ConstraintViolation<CreateAnswerRequest>> violations = validator.validate(createAnswerRequest);
		Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

		//then
		assertThat(msgList).isEqualTo(Set.of("회원 ID를 입력해 주세요.", "답변 내용을 입력해 주세요."));
	}

	@Test
	@DisplayName("답변 수정 요청 검증 테스트")
	void validateUpdateAnswerRequest() {
		UpdateAnswerRequest updateAnswerRequest = UpdateAnswerRequest.builder()
			.content("")
			.build();

		Set<ConstraintViolation<UpdateAnswerRequest>> violations = validator.validate(updateAnswerRequest);
		Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

		//then
		assertThat(msgList).isEqualTo(Set.of("답변 내용을 입력해 주세요."));
	}

}
