package com.kernelsquare.memberapi.common.util;

import com.kernelsquare.domainmongodb.domain.coffeechat.entity.MongoChatMessage;
import com.kernelsquare.domainmongodb.domain.coffeechat.entity.MongoMessageType;
import com.kernelsquare.memberapi.domain.coffeechat.dto.ChatMessage;

public class ChatMessageConverter {
	public static MongoChatMessage toMongoChatMessage(ChatMessage message) {
		return MongoChatMessage.builder()
			.roomKey(message.getRoomKey())
			.type(MongoMessageType.valueOf(String.valueOf(message.getType())))
			.sender(message.getSender())
			.message(message.getMessage())
			.sendTime(message.getSendTime())
			.build();
	}
}
