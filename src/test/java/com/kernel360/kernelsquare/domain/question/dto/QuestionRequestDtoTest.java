package com.kernel360.kernelsquare.domain.question.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Question 도메인 요청 Dto 종합 테스트")
class QuestionRequestDtoTest {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @Test
    @DisplayName("질문 생성 요청 검증 테스트")
    void validateCreateQuestionRequest() {
        CreateQuestionRequest createQuestionRequest = CreateQuestionRequest.builder()
            .title("")
            .content("")
            .build();

        Set<ConstraintViolation<CreateQuestionRequest>> violations = validator.validate(createQuestionRequest);
        Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        //then
        assertThat(msgList).isEqualTo(Set.of("회원 ID를 입력해 주세요.", "질문 제목을 입력해 주세요.",
            "질문 내용을 입력해 주세요.", "최소 빈 리스트로 입력해 주세요."));
    }

    @Test
    @DisplayName("질문 수정 요청 검증 테스트")
    void validateUpdateQuestionRequest() {
        UpdateQuestionRequest updateQuestionRequest = UpdateQuestionRequest.builder()
            .title("")
            .content("")
            .build();

        Set<ConstraintViolation<UpdateQuestionRequest>> violations = validator.validate(updateQuestionRequest);
        Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        //then
        assertThat(msgList).isEqualTo(Set.of("질문 제목을 입력해 주세요.", "질문 내용을 입력해 주세요.",
            "최소 빈 리스트로 입력해 주세요."));
    }

}