package com.kernelsquare.memberapi.domain.question.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Question 도메인 요청 Dto 종합 테스트")
class QuestionRequestDtoTest {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @Test
    @DisplayName("질문 생성 요청 검증 테스트 - NotNull, NotBlank")
    void whenCreateQuestionIsNotBlank_thenValidationFails() {
        CreateQuestionRequest createQuestionRequest = CreateQuestionRequest.builder()
                .title("")
                .content("")
                .build();

        Set<ConstraintViolation<CreateQuestionRequest>> violations = validator.validate(createQuestionRequest);
        Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        //then
        assertThat(msgList).isEqualTo(Set.of("회원 ID를 입력해 주세요.", "질문 제목을 입력해 주세요.",
                "질문 내용을 입력해 주세요.", "최소 빈 리스트로 입력해 주세요.", "질문 제목은 5자 이상 100자 이하로 작성해 주세요.", "질문 내용은 10자 이상 10000자 이하로 작성해 주세요."));
    }

    @Test
    @DisplayName("질문 생성 요청 검증 성공 테스트 - Size")
    void whenCreateAnswerSizeExceedsLimit_thenValidationSucceeds() {
        CreateQuestionRequest createQuestionRequest = CreateQuestionRequest.builder()
                .memberId(1L)
                .skills(List.of())
                .title("a".repeat(100))
                .content("a".repeat(10000))
                .build();

        Set<ConstraintViolation<CreateQuestionRequest>> violations = validator.validate(createQuestionRequest);
        Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        assertThat(msgList).isEqualTo(Set.of());
    }

    @Test
    @DisplayName("질문 생성 요청 검증 실패 테스트 - MaxSize")
    void whenCreateAnswerSizeExceedsMaxLimit_thenValidationFails() {
        CreateQuestionRequest createQuestionRequest = CreateQuestionRequest.builder()
                .memberId(1L)
                .skills(List.of())
                .title("a".repeat(101))
                .content("a".repeat(10001))
                .build();

        Set<ConstraintViolation<CreateQuestionRequest>> violations = validator.validate(createQuestionRequest);
        Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        assertThat(msgList).isEqualTo(Set.of("질문 제목은 5자 이상 100자 이하로 작성해 주세요.", "질문 내용은 10자 이상 10000자 이하로 작성해 주세요."));
    }

    @Test
    @DisplayName("질문 생성 요청 검증 실패 테스트 - MinSize")
    void whenCreateAnswerSizeExceedsMinLimit_thenValidationFails() {
        CreateQuestionRequest createQuestionRequest = CreateQuestionRequest.builder()
                .memberId(1L)
                .skills(List.of())
                .title("a")
                .content("a")
                .build();

        Set<ConstraintViolation<CreateQuestionRequest>> violations = validator.validate(createQuestionRequest);
        Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        assertThat(msgList).isEqualTo(Set.of("질문 제목은 5자 이상 100자 이하로 작성해 주세요.", "질문 내용은 10자 이상 10000자 이하로 작성해 주세요."));
    }

    @Test
    @DisplayName("질문 수정 요청 검증 테스트 - NotNull, NotBlank")
    void whenUpdateQuestionIsNotBlank_thenValidationFails() {
        UpdateQuestionRequest updateQuestionRequest = UpdateQuestionRequest.builder()
                .title("")
                .content("")
                .build();

        Set<ConstraintViolation<UpdateQuestionRequest>> violations = validator.validate(updateQuestionRequest);
        Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        //then
        assertThat(msgList).isEqualTo(Set.of("질문 제목을 입력해 주세요.", "질문 내용을 입력해 주세요.",
                "최소 빈 리스트로 입력해 주세요.", "질문 제목은 5자 이상 100자 이하로 작성해 주세요.", "질문 내용은 10자 이상 10000자 이하로 작성해 주세요."));
    }

    @Test
    @DisplayName("질문 수정 요청 검증 성공 테스트 - Size")
    void whenUpdateAnswerSizeExceedsLimit_thenValidationSucceeds() {
        UpdateQuestionRequest updateQuestionRequest = UpdateQuestionRequest.builder()
                .skills(List.of())
                .title("a".repeat(100))
                .content("a".repeat(10000))
                .build();

        Set<ConstraintViolation<UpdateQuestionRequest>> violations = validator.validate(updateQuestionRequest);
        Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        assertThat(msgList).isEqualTo(Set.of());
    }

    @Test
    @DisplayName("질문 수정 요청 검증 실패 테스트 - MaxSize")
    void whenUpdateAnswerSizeExceedsMaxLimit_thenValidationFails() {
        UpdateQuestionRequest updateQuestionRequest = UpdateQuestionRequest.builder()
                .skills(List.of())
                .title("a".repeat(101))
                .content("a".repeat(10001))
                .build();

        Set<ConstraintViolation<UpdateQuestionRequest>> violations = validator.validate(updateQuestionRequest);
        Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        assertThat(msgList).isEqualTo(Set.of("질문 제목은 5자 이상 100자 이하로 작성해 주세요.", "질문 내용은 10자 이상 10000자 이하로 작성해 주세요."));
    }

    @Test
    @DisplayName("질문 수정 요청 검증 실패 테스트 - MinSize")
    void whenUpdateAnswerSizeExceedsMinLimit_thenValidationFails() {
        UpdateQuestionRequest updateQuestionRequest = UpdateQuestionRequest.builder()
                .skills(List.of())
                .title("a")
                .content("a")
                .build();

        Set<ConstraintViolation<UpdateQuestionRequest>> violations = validator.validate(updateQuestionRequest);
        Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        assertThat(msgList).isEqualTo(Set.of("질문 제목은 5자 이상 100자 이하로 작성해 주세요.", "질문 내용은 10자 이상 10000자 이하로 작성해 주세요."));
    }
}
