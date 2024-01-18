package com.kernel360.kernelsquare.domain.coffeechat.dto;

import lombok.Builder;

@Builder
public record EnterCoffeeChatRoomRequest(
    Long memberId,
    Long roomId,
    String articleTitle
) {
}
