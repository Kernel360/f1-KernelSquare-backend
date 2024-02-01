package com.kernel360.kernelsquare.domain.coffeechat.controller;

import com.kernel360.kernelsquare.domain.coffeechat.dto.ChatMessage;
import com.kernel360.kernelsquare.global.common_response.error.code.CoffeeChatErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
class TestMessageController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @MessageMapping("/test/message")
    public void messageHandler(ChatMessage message) {

        switch (message.getType()) {
            case ENTER -> message.setMessage(message.getSender()+"님이 입장하였습니다.");
            case LEAVE -> message.setMessage(message.getSender()+"님이 퇴장하였습니다.");
            case TALK -> {}
            default -> throw new BusinessException(CoffeeChatErrorCode.MESSAGE_TYPE_NOT_VALID);
        }

        messagingTemplate.convertAndSend("/topic/test/room/" + message.getRoomKey(), message);
    }
}