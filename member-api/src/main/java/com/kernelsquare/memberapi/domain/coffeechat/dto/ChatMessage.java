package com.kernelsquare.memberapi.domain.coffeechat.dto;

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
public class ChatMessage {

	private MessageType type;

	private String roomKey;

	private String sender;

	private String message;

	private LocalDateTime sendTime;

	public void setMessage(String message) {
		this.message = message;
	}
}

