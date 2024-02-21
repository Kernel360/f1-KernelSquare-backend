package com.kernelsquare.memberapi.domain.member.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRequestDtoTest {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @Test
    @DisplayName("자기소개 수정 요청 검증 테스트 - NotNull")
    void whenUpdateMemberIntroductionIsNotNull_thenValidationFails() {
        UpdateMemberIntroductionRequest updateMemberIntroductionRequest = UpdateMemberIntroductionRequest.builder()
                .introduction(null)
                .build();

        Set<ConstraintViolation<UpdateMemberIntroductionRequest>> violations = validator.validate(
                updateMemberIntroductionRequest);
        Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        assertThat(msgList).isEqualTo(Set.of("자기 소개글 수정은 null일 수 없습니다."));
    }

    @Test
    @DisplayName("자기소개 수정 요청 검증 성공 테스트 - Size")
    void whenUpdateMemberIntroductionSizeExceedsLimit_thenValidationSucceeds() {
        UpdateMemberIntroductionRequest updateMemberIntroductionRequest = UpdateMemberIntroductionRequest.builder()
                .introduction("a".repeat(1000))
                .build();

        Set<ConstraintViolation<UpdateMemberIntroductionRequest>> violations = validator.validate((updateMemberIntroductionRequest));
        Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        assertThat(msgList).isEqualTo(Set.of());
    }

    @Test
    @DisplayName("자기소개 수정 요청 검증 실패 테스트 - MaxSize")
    void whenUpdateMemberIntroductionMaxSizeExceedsLimit_thenValidationFails() {
        UpdateMemberIntroductionRequest updateMemberIntroductionRequest = UpdateMemberIntroductionRequest.builder()
                .introduction("a".repeat(1001))
                .build();

        Set<ConstraintViolation<UpdateMemberIntroductionRequest>> violations = validator.validate((updateMemberIntroductionRequest));
        Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        assertThat(msgList).isEqualTo(Set.of("자기 소개글은 10자 이상 1000자 이하로 작성해 주세요."));
    }

    @Test
    @DisplayName("자기소개 수정 요청 검증 실패 테스트 - MinSize")
    void whenUpdateMemberIntroductionMinSizeExceedsLimit_thenValidationFails() {
        UpdateMemberIntroductionRequest updateMemberIntroductionRequest = UpdateMemberIntroductionRequest.builder()
                .introduction("a".repeat(1001))
                .build();

        Set<ConstraintViolation<UpdateMemberIntroductionRequest>> violations = validator.validate((updateMemberIntroductionRequest));
        Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        assertThat(msgList).isEqualTo(Set.of("자기 소개글은 10자 이상 1000자 이하로 작성해 주세요."));
    }
}
