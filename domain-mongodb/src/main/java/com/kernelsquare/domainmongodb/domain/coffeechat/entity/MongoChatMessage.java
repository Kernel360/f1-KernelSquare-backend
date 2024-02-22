package com.kernelsquare.domainmongodb.domain.coffeechat.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Builder
@Getter
@Document(collection = "chatting")
public class MongoChatMessage {

	@Id
	private String id;

	@Indexed
	private String roomKey;

	private MongoMessageType type;

	private Long senderId;

	private String sender;

	private String senderImageUrl;

	private String message;

	private LocalDateTime sendTime;
}
