package com.kernelsquare.adminapi.domain.member.dto;

import com.kernelsquare.core.type.AuthorityType;
import com.kernelsquare.core.validation.annotations.EnumValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public class MemberDto {
    @Builder
    public record UpdateAuthorityRequest(
        @NotNull(message = "변경시키고자 하는 회원 ID는 필수 입력사항입니다.")
        Long memberId,
        @EnumValue(enumClass = AuthorityType.class, message = "유효한 회원 권한을 선택해주세요.")
        String authorityType
    ) {}

    @Builder
    public record UpdateNicknameRequest(
        @NotNull(message = "변경시키고자 하는 회원 ID는 필수 입력사항입니다.")
        Long memberId,
        @NotBlank(message = "변경시키고자 하는 회원 닉네임은 필수 입력사항입니다.")
        String nickname
    ) {}

    @Builder
    public record FindResponse(
        Long memberId,
        String nickname,
        Long experience,
        String introduction,
        String imageUrl,
        Long level
    ) {}
}
