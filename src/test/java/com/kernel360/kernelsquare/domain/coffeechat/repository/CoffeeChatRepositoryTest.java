package com.kernel360.kernelsquare.domain.coffeechat.repository;

import com.kernel360.kernelsquare.domain.coffeechat.entity.ChatRoom;
import com.kernel360.kernelsquare.global.common_response.error.code.CoffeeChatErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import com.kernel360.kernelsquare.global.config.JpaAuditingConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("커피챗 레포지토리 단위 테스트")
@DataJpaTest
@Import(JpaAuditingConfig.class)
class CoffeeChatRepositoryTest {
    @Autowired
    private CoffeeChatRepository coffeeChatRepository;

    @Test
    @DisplayName("커피챗 findByRoomKey 정상 작동 테스트")
    void testFindByRoomKey() {
        //given
        ChatRoom chatRoom = ChatRoom.builder()
            .roomKey("asdf")
            .build();

        ChatRoom saveChatRoom = coffeeChatRepository.save(chatRoom);

        //when
        ChatRoom findChatRoom = coffeeChatRepository.findByRoomKey(chatRoom.getRoomKey())
            .orElseThrow(() -> new BusinessException(CoffeeChatErrorCode.COFFEE_CHAT_ROOM_NOT_FOUND));

        //then
        assertThat(findChatRoom).isNotNull();
        assertThat(findChatRoom).isEqualTo(saveChatRoom);
    }
}