package com.kernelsquare.domainmongodb.domain.coffeechat.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.kernelsquare.domainmongodb.domain.coffeechat.entity.MongoChatMessage;

import java.util.List;

public interface MongoChatMessageRepository extends MongoRepository<MongoChatMessage, String> {
    List<MongoChatMessage> findAllByRoomKey(String roomKey);
}
