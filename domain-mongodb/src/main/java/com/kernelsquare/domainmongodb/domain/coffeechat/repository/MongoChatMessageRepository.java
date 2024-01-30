package com.kernelsquare.domainmongodb.domain.coffeechat.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.kernel360.kernelsquare.domain.coffeechat.entity.MongoChatMessage;

public interface MongoChatMessageRepository extends MongoRepository<MongoChatMessage, String> {
}
