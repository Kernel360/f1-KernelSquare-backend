package com.kernelsquare.domainmysql.domain.coffeechat.repository;

import com.kernelsquare.core.common_response.error.code.CoffeeChatErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.config.DBConfig;
import com.kernelsquare.domainmysql.config.JpaConfig;
import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("커피챗 레포지토리 단위 테스트")
@DataJpaTest
@Import(DBConfig.class)
class CoffeeChatRepositoryTest {
    @Autowired
    private CoffeeChatRepository coffeeChatRepository;

    @Test
    @DisplayName("커피챗 findByRoomKey 정상 작동 테스트")
    void testFindByRoomKey() {
        //given
        ChatRoom chatRoom = ChatRoom.builder()
            .roomKey("asdf")
            .expirationTime(LocalDateTime.now().plusMinutes(30))
            .build();

        ChatRoom saveChatRoom = coffeeChatRepository.save(chatRoom);

        //when
        ChatRoom findChatRoom = coffeeChatRepository.findByRoomKey(chatRoom.getRoomKey())
            .orElseThrow(() -> new BusinessException(CoffeeChatErrorCode.COFFEE_CHAT_ROOM_NOT_FOUND));

        //then
        assertThat(findChatRoom).isNotNull();
        assertThat(findChatRoom).isEqualTo(saveChatRoom);
    }

    @Test
    @DisplayName("커피챗 findAllByActive 정상 작동 테스트")
    void testFindAllByActive() {
        //given
        ChatRoom chatRoom = ChatRoom.builder()
            .roomKey("asdf")
            .expirationTime(LocalDateTime.now().plusMinutes(30))
            .build();

        ChatRoom saveChatRoom = coffeeChatRepository.save(chatRoom);

        //when
        List<ChatRoom> findChatRoomList = coffeeChatRepository.findAllByActive(false);

        //then
        assertThat(findChatRoomList).isNotNull();
        assertThat(findChatRoomList.get(0)).isEqualTo(saveChatRoom);
    }
}