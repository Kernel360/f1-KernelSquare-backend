package com.kernel360.kernelsquare.domain.member_answer_vote.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MeberAnswerVote 도메인 요청 Dto 종합 테스트")
class MemberAnswerVoteRequestDtoTest {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @Test
    @DisplayName("투표 생성 요청 검증 테스트")
    void validateCreateMemberAnswerVoteRequest() {
        CreateMemberAnswerVoteRequest createMemberAnswerVoteRequest = CreateMemberAnswerVoteRequest.builder()
            .build();

        Set<ConstraintViolation<CreateMemberAnswerVoteRequest>> violations = validator.validate(createMemberAnswerVoteRequest);
        Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        //then
        assertThat(msgList).isEqualTo(Set.of("회원 ID를 입력해 주세요.", "Status는 1 또는 -1 이어야 합니다."));
    }
}