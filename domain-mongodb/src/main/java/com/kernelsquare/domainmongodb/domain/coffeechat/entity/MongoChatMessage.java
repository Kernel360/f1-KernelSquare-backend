package com.kernelsquare.domainmongodb.domain.coffeechat.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Document(collection = "chatting")
public class MongoChatMessage {

	@Id
	private String id;

	@Indexed
	private String roomKey;

	private MongoMessageType type;

	private String sender;

	private String message;

	private LocalDateTime sendTime;
}
