package com.kernel360.kernelsquare.domain.coffeechat.dto;

import com.kernel360.kernelsquare.domain.coffeechat.entity.MongoChatMessage;
import lombok.Builder;

import java.util.List;

@Builder
public record GetChatHistoryResponse(
    List<MongoChatMessage> chatHistory
) {
    public static GetChatHistoryResponse of(List<MongoChatMessage> chatHistory) {
        return GetChatHistoryResponse.builder()
            .chatHistory(chatHistory)
            .build();
    }
}
