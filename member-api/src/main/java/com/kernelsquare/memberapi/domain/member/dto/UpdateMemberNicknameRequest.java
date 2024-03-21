package com.kernelsquare.memberapi.domain.member.dto;

import com.kernelsquare.core.validation.annotations.BadWordFilter;
import com.kernelsquare.core.validation.constants.BadWordValidationMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateMemberNicknameRequest(
        @NotNull(message = "변경시키고자 하는 회원 ID는 필수 입력사항입니다.")
        Long memberId,
        @NotBlank(message = "변경시키고자 하는 회원 닉네임은 필수 입력사항입니다.")
        @BadWordFilter(message = BadWordValidationMessage.NO_BAD_WORD_IN_CONTENT)
        String nickname
) {
}
