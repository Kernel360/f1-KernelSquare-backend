package com.kernelsquare.domainmongodb.domain.coffeechat.repository;

import com.kernelsquare.domainmongodb.domain.coffeechat.entity.MongoChatMessage;
import com.kernelsquare.domainmongodb.domain.coffeechat.entity.MongoMessageType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("몽고 DB 단위 테스트")
@DataMongoTest
class MongoChatMessageRepositoryTest {
    @Autowired
    private MongoChatMessageRepository mongoChatMessageRepository;

    @Test
    @DisplayName("몽고 DB findAllByRoomKey 정상 작동 테스트")
    void testFindAllByRoomKey() {
        //given
        MongoChatMessage message = MongoChatMessage.builder()
            .roomKey("key1")
            .sendTime(LocalDateTime.now())
            .message("메시지1")
            .sender("김아무개")
            .type(MongoMessageType.TALK)
            .build();

        MongoChatMessage saveMessage = mongoChatMessageRepository.save(message);

        //when
        List<MongoChatMessage> chatHistory = mongoChatMessageRepository.findAllByRoomKey(message.getRoomKey());

        //then
        assertThat(chatHistory).isNotNull();
        assertThat(chatHistory.getLast().getMessage()).isEqualTo(saveMessage.getMessage());
        assertThat(chatHistory.getLast().getSender()).isEqualTo(saveMessage.getSender());
        assertThat(chatHistory.getLast().getSendTime()).isEqualTo(saveMessage.getSendTime().truncatedTo(ChronoUnit.MILLIS));
        assertThat(chatHistory.getLast().getRoomKey()).isEqualTo(saveMessage.getRoomKey());
        assertThat(chatHistory.getLast().getType()).isEqualTo(saveMessage.getType());
        assertThat(chatHistory.getLast().getId()).isEqualTo(saveMessage.getId());
    }
}