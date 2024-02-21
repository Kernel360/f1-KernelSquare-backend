package com.kernelsquare.memberapi.common.util;

import com.kernelsquare.domainmongodb.domain.coffeechat.entity.MongoChatMessage;
import com.kernelsquare.domainmongodb.domain.coffeechat.entity.MongoMessageType;
import com.kernelsquare.memberapi.domain.coffeechat.dto.ChatMessageRequest;

public class ChatMessageConverter {
	public static MongoChatMessage toMongoChatMessage(ChatMessageRequest message) {
		return MongoChatMessage.builder()
			.roomKey(message.getRoomKey())
			.type(MongoMessageType.valueOf(String.valueOf(message.getType())))
			.senderId(message.getSenderId())
			.sender(message.getSender())
			.senderImageUrl(message.getSenderImageUrl())
			.message(message.getMessage())
			.sendTime(message.getSendTime())
			.build();
	}
}
