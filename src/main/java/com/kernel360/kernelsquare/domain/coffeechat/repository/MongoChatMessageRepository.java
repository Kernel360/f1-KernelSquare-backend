package com.kernel360.kernelsquare.domain.coffeechat.repository;

import com.kernel360.kernelsquare.domain.coffeechat.entity.MongoChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoChatMessageRepository extends MongoRepository<MongoChatMessage, String> {
    List<MongoChatMessage> findAllByRoomKey(String roomKey);
}
