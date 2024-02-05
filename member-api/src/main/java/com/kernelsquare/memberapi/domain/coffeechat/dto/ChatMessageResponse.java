package com.kernelsquare.memberapi.domain.coffeechat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kernelsquare.core.type.MessageType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse {

	private MessageType type;

	private String roomKey;

	private String sender;

	private String message;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	private LocalDateTime sendTime;

	public static ChatMessageResponse convertResponse(ChatMessageRequest chatMessageRequest) {
		return ChatMessageResponse.builder()
			.type(chatMessageRequest.getType())
			.roomKey(chatMessageRequest.getRoomKey())
			.sender(chatMessageRequest.getSender())
			.message(chatMessageRequest.getMessage())
			.sendTime(chatMessageRequest.getSendTime())
			.build();
	}
}

