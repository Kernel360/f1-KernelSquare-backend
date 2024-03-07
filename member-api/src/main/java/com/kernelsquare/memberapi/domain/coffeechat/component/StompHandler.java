package com.kernelsquare.memberapi.domain.coffeechat.component;

import com.kernelsquare.core.common_response.error.code.CoffeeChatErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Objects;

/* 메시지가 보내진 후 해당 메시지를 인터셉트하여 핸들링하기 위한 클래스 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {
    private final ChatRoomMemberManager chatRoomMemberManager;
    @Override
    public void postSend(Message message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();

        if (Objects.isNull(command)) {
            throw new BusinessException(CoffeeChatErrorCode.MESSAGE_COMMAND_NOT_VALID);
        }

        switch (command) {
            case SUBSCRIBE -> handleSubscribeEvent(message);
            default -> {}
        }
    }

    private void handleSubscribeEvent(Message message) {
        StompMessageParser parser = new StompMessageParser(message);

        String roomKey = parser.getRoomKey();
        Long memberId = parser.getMemberId();

        chatRoomMemberManager.addChatMember(roomKey, memberId);
    }
}