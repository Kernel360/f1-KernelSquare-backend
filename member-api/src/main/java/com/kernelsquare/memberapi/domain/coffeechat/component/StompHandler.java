package com.kernelsquare.memberapi.domain.coffeechat.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {
    private final ChatRoomMemberManager chatRoomMemberManager;
    @Override
    public void postSend(Message message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        switch (accessor.getCommand()) {
            case SUBSCRIBE -> handleSubscribeEvent(message);
            case DISCONNECT -> handleDisconnectEvent(message);
            default -> {}
        }
    }

    private void handleSubscribeEvent(Message message) {
        StompMessageParser parser = new StompMessageParser(message);

        String roomKey = parser.getRoomKey();
        Long memberId = parser.getMemberId();

        chatRoomMemberManager.addChatMember(roomKey, memberId);
    }

    private void handleDisconnectEvent(Message message) {
        StompMessageParser parser = new StompMessageParser(message);

        String roomKey = parser.getRoomKey();
        Long memberId = parser.getMemberId();

        chatRoomMemberManager.removeChatRoomMember(roomKey, memberId);
    }
}