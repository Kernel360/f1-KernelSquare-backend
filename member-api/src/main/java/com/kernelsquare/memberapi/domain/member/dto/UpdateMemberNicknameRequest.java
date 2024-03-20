package com.kernelsquare.memberapi.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateMemberNicknameRequest(
        @NotNull(message = "변경시키고자 하는 회원 ID는 필수 입력사항입니다.")
        Long memberId,
        @NotBlank(message = "변경시키고자 하는 회원 닉네임은 필수 입력사항입니다.")
        String nickname
) {
}
