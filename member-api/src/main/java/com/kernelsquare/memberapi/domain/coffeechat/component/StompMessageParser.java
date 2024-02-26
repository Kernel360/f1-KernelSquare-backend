package com.kernelsquare.memberapi.domain.coffeechat.component;

import com.kernelsquare.core.common_response.error.code.CoffeeChatErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/* STOMP 메시지 헤더에서 필요한 데이터를 가져오기 위한 클래스 */
public class StompMessageParser {
    private final StompHeaderAccessor accessor;

    public StompMessageParser(Message message) {
        this.accessor = StompHeaderAccessor.wrap(message);
    }

    public String getRoomKey() {
        if (Objects.equals(accessor.getCommand(), StompCommand.SUBSCRIBE)) {
            String destination = Optional.ofNullable(accessor.getDestination())
                .orElseThrow(() -> new BusinessException(CoffeeChatErrorCode.MESSAGE_DESTINATION_NOT_VALID));

            return Arrays.stream(destination.split("/"))
                .reduce((first, second) -> second)
                .get();
        }

        String roomKey = Optional.ofNullable(accessor.getNativeHeader("roomKey").getFirst())
            .orElseThrow(() -> new BusinessException(CoffeeChatErrorCode.MESSAGE_NATIVEHEADER_NOT_FOUND));

        return roomKey;
    }

    public Long getMemberId() {
        Long memberId = Optional.ofNullable(Long.valueOf(accessor.getNativeHeader("memberId").getFirst()))
            .orElseThrow(() -> new BusinessException(CoffeeChatErrorCode.MESSAGE_NATIVEHEADER_NOT_FOUND));

        return memberId;
    }
}
