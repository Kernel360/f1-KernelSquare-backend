package com.kernelsquare.memberapi.domain.coffeechat.dto;

import com.kernelsquare.core.type.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequest {

    private MessageType type;

    private String roomKey;

    private Long senderId;

    private String sender;

    private String senderImageUrl;

    private String message;

    private LocalDateTime sendTime;

    private List<ChatRoomMember> memberList;

    public void setMessage(String message) {
        this.message = message;
    }
}
