package com.kernelsquare.adminapi.domain.auth.dto;

import com.kernelsquare.core.validation.ValidationSequence;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Auth 도메인 요청 Dto 종합 테스트")
class AuthRequestDtoTest {
	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private final Validator validator = factory.getValidator();

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