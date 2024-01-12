package com.kernel360.kernelsquare.domain.level.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Level 도메인 요청 Dto 종합 테스트")
class LevelRequestDtoTest {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @Test
    @DisplayName("레벨 생성 요청 검증 테스트")
    void validateCreateLevelRequest() {
        CreateLevelRequest createLevelRequest = CreateLevelRequest.builder()
            .imageUrl("")
            .build();

        Set<ConstraintViolation<CreateLevelRequest>> violations = validator.validate(createLevelRequest);
        Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        //then
        assertThat(msgList).isEqualTo(Set.of("등급 이름을 입력해 주세요.", "등급 이미지 URL을 입력해 주세요.",
            "등급 경험치 상한선을 입력해 주세요."));
    }

    @Test
    @DisplayName("레벨 수정 요청 검증 테스트")
    void validateUpdateLevelRequest() {
        UpdateLevelRequest updateLevelRequest = UpdateLevelRequest.builder()
            .imageUrl("")
            .build();

        Set<ConstraintViolation<UpdateLevelRequest>> violations = validator.validate(updateLevelRequest);
        Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        //then
        assertThat(msgList).isEqualTo(Set.of("등급 이름을 입력해 주세요.", "등급 이미지 URL을 입력해 주세요.",
            "등급 경험치 상한선을 입력해 주세요."));
    }
}