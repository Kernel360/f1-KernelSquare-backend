package com.kernelsquare.memberapi.domain.coffeechat.dto;

import com.kernelsquare.core.type.MessageType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

	private MessageType type;

	private String roomKey;

	private String sender;

	private String message;

	public void setMessage(String message) {
		this.message = message;
	}
}

