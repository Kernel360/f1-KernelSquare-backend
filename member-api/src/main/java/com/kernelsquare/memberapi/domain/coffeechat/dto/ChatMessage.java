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
public class ChatMessage {

	private MessageType type;

	private String roomKey;

	private String sender;

	private String message;

	// TODO 메시지 받을 때도 이 DTO를 사용하기 때문에, 만약 message 받는 것에 문제가 생긴다면 request와 response로 나눌 것
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	private LocalDateTime sendTime;

	public void setMessage(String message) {
		this.message = message;
	}
}

