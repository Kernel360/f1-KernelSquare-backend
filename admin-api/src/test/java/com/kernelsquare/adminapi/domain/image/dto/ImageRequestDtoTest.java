package com.kernelsquare.adminapi.domain.image.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Image 도메인 요청 Dto 단위 테스트")
public class ImageRequestDtoTest {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @Test
    @DisplayName("날짜별 이미지 조회 요청 검증 실패 테스트 - Size")
    void whenFindAllImagesSizeExceedsLimit_thenValidationFails() {
        ImageDto.FindAllRequest request = ImageDto.FindAllRequest.builder()
            .createdDate("911212")
            .build();

        Set<ConstraintViolation<ImageDto.FindAllRequest>> violations = validator.validate(request);
        Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        assertThat(msgList).isEqualTo(Set.of("yyyyMMdd 형식의 8글자로 입력해 주세요."));
    }

    @Test
    @DisplayName("날짜별 이미지 조회 요청 검증 성공 테스트")
    void whenFindAllImagesSizeExceedsLimit_thenValidationSucceeds() {
        ImageDto.FindAllRequest request = ImageDto.FindAllRequest.builder()
            .createdDate("19911212")
            .build();

        Set<ConstraintViolation<ImageDto.FindAllRequest>> violations = validator.validate(request);
        Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        assertThat(msgList).isEqualTo(Set.of());
    }

}
