package com.kernel360.kernelsquare.domain.coffeechat.dto;

import com.kernel360.kernelsquare.domain.coffeechat.entity.MongoChatMessage;
import lombok.Builder;

import java.util.List;

@Builder
public record FindChatHistoryResponse(
    List<MongoChatMessage> chatHistory
) {
    public static FindChatHistoryResponse of(List<MongoChatMessage> chatHistory) {
        return FindChatHistoryResponse.builder()
            .chatHistory(chatHistory)
            .build();
    }
}
