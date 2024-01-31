package com.kernelsquare.adminapi.domain.tech_stack.dto;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@DisplayName("TechStack 도메인 요청 Dto 종합 테스트")
class TechStackRequestDtoTest {
	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	Validator validator = factory.getValidator();

	@Test
	@DisplayName("기술 스택 생성 요청 검증 테스트")
	void validateCreateTechStackRequest() {
		CreateTechStackRequest createTechStackRequest = CreateTechStackRequest.builder()
			.skill("")
			.build();

		Set<ConstraintViolation<CreateTechStackRequest>> violations = validator.validate(createTechStackRequest);
		Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

		//then
		assertThat(msgList).isEqualTo(Set.of("기술 스택을 입력해 주세요."));
	}

	@Test
	@DisplayName("기술 스택 수정 요청 검증 테스트")
	void validateUpdateTechStackRequest() {
		UpdateTechStackRequest updateTechStackRequest = UpdateTechStackRequest.builder()
			.skill("")
			.build();

		Set<ConstraintViolation<UpdateTechStackRequest>> violations = validator.validate(updateTechStackRequest);
		Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

		//then
		assertThat(msgList).isEqualTo(Set.of("기술 스택을 입력해 주세요."));
	}
}