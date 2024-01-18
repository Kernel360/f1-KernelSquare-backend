package com.kernel360.kernelsquare.domain.coffeechat.controller;

import com.kernel360.kernelsquare.domain.coffeechat.dto.ChatMessage;
import com.kernel360.kernelsquare.global.common_response.error.code.CoffeeChatErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessageSendingOperations sendingOperations;

    @MessageMapping("/chat/message")
    public void messageHandler(ChatMessage message) {
        switch (message.getType()) {
            case ENTER -> message.setMessage(message.getSender()+"님이 입장하였습니다.");
            case LEAVE -> message.setMessage(message.getSender()+"님이 퇴장하였습니다.");
            case TALK -> {}
            default -> throw new BusinessException(CoffeeChatErrorCode.MESSAGE_TYPE_NOT_VALID);
        }
        sendingOperations.convertAndSend("/topic/chat/room/"+message.getRoomKey(),message);
    }
}
