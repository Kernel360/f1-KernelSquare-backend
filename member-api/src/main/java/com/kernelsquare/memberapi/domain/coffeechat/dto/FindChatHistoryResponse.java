package com.kernelsquare.memberapi.domain.coffeechat.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record FindChatHistoryResponse(
    List<FindMongoChatMessage> chatHistory
) {
    public static FindChatHistoryResponse of(List<FindMongoChatMessage> chatHistory) {
        return FindChatHistoryResponse.builder()
            .chatHistory(chatHistory)
            .build();
    }
}
