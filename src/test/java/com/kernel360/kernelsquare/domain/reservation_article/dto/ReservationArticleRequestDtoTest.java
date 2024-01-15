package com.kernel360.kernelsquare.domain.reservation_article.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ReservationArticle 도메인 요청 Dto 종합 테스트")
class ReservationArticleRequestDtoTest {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @Test
    @DisplayName("예약창 생성 요청 검증 테스트")
    void validateCreateReservationArticleRequest() {
        CreateReservationArticleRequest createReservationArticleRequest = CreateReservationArticleRequest.builder()
            .title("")
            .content("")
            .build();

        Set<ConstraintViolation<CreateReservationArticleRequest>> violations = validator.validate(createReservationArticleRequest);
        Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        //then
        assertThat(msgList).isEqualTo(Set.of("회원 ID를 입력해 주세요.", "예약창 제목을 입력해 주세요.",
            "예약창 내용을 입력해 주세요.", "최소 빈 리스트로 입력해 주세요.", "예약 시간을 입력해 주세요."));
    }
}