package com.kernelsquare.memberapi.domain.coffeechat.dto;

import com.kernelsquare.domainmongodb.domain.coffeechat.entity.MongoChatMessage;
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
