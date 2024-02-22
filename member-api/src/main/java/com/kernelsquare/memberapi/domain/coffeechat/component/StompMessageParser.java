package com.kernelsquare.memberapi.domain.coffeechat.component;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import java.util.Objects;

public class StompMessageParser {
    private final StompHeaderAccessor accessor;

    public StompMessageParser(Message message) {
        this.accessor = StompHeaderAccessor.wrap(message);
    }

    public String getRoomKey() {
        if (Objects.equals(accessor.getCommand(), StompCommand.SUBSCRIBE)) {
            String destination = accessor.getDestination();
            String[] destinationSplit = destination.split("/");

            return destinationSplit[destinationSplit.length - 1];
        }

        return accessor.getNativeHeader("roomKey").getFirst();
    }

    public Long getMemberId() {
        return Long.valueOf(accessor.getNativeHeader("memberId").getFirst());
    }
}
