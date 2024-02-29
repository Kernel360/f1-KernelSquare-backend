package com.kernelsquare.memberapi.domain.coffeechat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kernelsquare.core.constants.TimeResponseFormat;
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
public class ChatMessageResponse {

	private MessageType type;

	private String roomKey;

	private Long senderId;

	private String sender;

	private String senderImageUrl;

	private String message;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
	private LocalDateTime sendTime;

	private List<ChatRoomMember> memberList;

	public static ChatMessageResponse convertResponse(ChatMessageRequest chatMessageRequest) {
		return ChatMessageResponse.builder()
			.type(chatMessageRequest.getType())
			.roomKey(chatMessageRequest.getRoomKey())
			.senderId(chatMessageRequest.getSenderId())
			.sender(chatMessageRequest.getSender())
			.senderImageUrl(chatMessageRequest.getSenderImageUrl())
			.message(chatMessageRequest.getMessage())
			.sendTime(chatMessageRequest.getSendTime())
			.memberList(chatMessageRequest.getMemberList())
			.build();
	}
}

