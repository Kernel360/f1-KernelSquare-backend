package com.kernelsquare.adminapi.domain.member.dto;

import com.kernelsquare.core.type.AuthorityType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Member 도메인 요청 Dto 단위 테스트")
public class MemberRequestDtoTest {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @Test
    @DisplayName("회원 권한 수정 요청 검증 실패 테스트 - NotNull")
    void whenUpdateMemberAuthorityIsNull_thenValidationFails() {
        MemberDto.UpdateAuthorityRequest request = MemberDto.UpdateAuthorityRequest.builder()
            .authorityType(AuthorityType.ROLE_MENTOR.getDescription())
            .build();

        Set<ConstraintViolation<MemberDto.UpdateAuthorityRequest>> violations = validator.validate(request);
        Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        //then
        assertThat(msgList).isEqualTo(Set.of("변경시키고자 하는 회원 ID는 필수 입력사항입니다."));
    }

    @Test
    @DisplayName("회원 권한 수정 요청 검증 실패 테스트 - EnumValue")
    void whenUpdateMemberAuthorityIsValidEnumValue_thenValidationFails() {
        MemberDto.UpdateAuthorityRequest request = MemberDto.UpdateAuthorityRequest.builder()
            .memberId(2L)
            .authorityType("ROLE_MENTEE")
            .build();

        Set<ConstraintViolation<MemberDto.UpdateAuthorityRequest>> violations = validator.validate(request);
        Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        //then
        assertThat(msgList).isEqualTo(Set.of("유효한 회원 권한을 선택해주세요."));
    }

    @Test
    @DisplayName("회원 권한 수정 요청 검증 성공 테스트")
    void whenUpdateMemberAuthorityIsNotNullAndValidEnumValue_thenValidationSucceeds() {
        MemberDto.UpdateAuthorityRequest request = MemberDto.UpdateAuthorityRequest.builder()
            .memberId(2L)
            .authorityType(AuthorityType.ROLE_USER.getDescription())
            .build();

        Set<ConstraintViolation<MemberDto.UpdateAuthorityRequest>> violations = validator.validate(request);
        Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        //then
        assertThat(msgList).isEqualTo(Set.of());
    }

    @Test
    @DisplayName("회원 닉네임 수정 요청 검증 실패 테스트 - NotNull")
    void whenUpdateMemberNicknameIsNull_thenValidationFails() {
        MemberDto.UpdateNicknameRequest request = MemberDto.UpdateNicknameRequest.builder()
            .nickname("하이연")
            .build();

        Set<ConstraintViolation<MemberDto.UpdateNicknameRequest>> violations = validator.validate(request);
        Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        //then
        assertThat(msgList).isEqualTo(Set.of("변경시키고자 하는 회원 ID는 필수 입력사항입니다."));
    }

    @Test
    @DisplayName("회원 닉네임 수정 요청 검증 실패 테스트 - NotBlank")
    void whenUpdateMemberNicknameIsBlank_thenValidationFails() {
        MemberDto.UpdateNicknameRequest request = MemberDto.UpdateNicknameRequest.builder()
            .memberId(4L)
            .nickname("   ")
            .build();

        Set<ConstraintViolation<MemberDto.UpdateNicknameRequest>> violations = validator.validate(request);
        Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        //then
        assertThat(msgList).isEqualTo(Set.of("변경시키고자 하는 회원 닉네임은 필수 입력사항입니다."));
    }

    @Test
    @DisplayName("회원 닉네임 수정 요청 검증 성공 테스트")
    void whenUpdateMemberNicknameIsNotNullAndNotBlank_thenValidationSucceeds() {
        MemberDto.UpdateNicknameRequest request = MemberDto.UpdateNicknameRequest.builder()
            .memberId(4L)
            .nickname("하이연")
            .build();

        Set<ConstraintViolation<MemberDto.UpdateNicknameRequest>> violations = validator.validate(request);
        Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        //then
        assertThat(msgList).isEqualTo(Set.of());
    }
}
