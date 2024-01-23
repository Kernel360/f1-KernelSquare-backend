package com.kernel360.kernelsquare.domain.coffeechat.service;

import com.kernel360.kernelsquare.domain.coffeechat.dto.CreateCoffeeChatRoomRequest;
import com.kernel360.kernelsquare.domain.coffeechat.dto.CreateCoffeeChatRoomResponse;
import com.kernel360.kernelsquare.domain.coffeechat.dto.EnterCoffeeChatRoomRequest;
import com.kernel360.kernelsquare.domain.coffeechat.dto.EnterCoffeeChatRoomResponse;
import com.kernel360.kernelsquare.domain.coffeechat.entity.ChatRoom;
import com.kernel360.kernelsquare.domain.coffeechat.repository.CoffeeChatRepository;
import com.kernel360.kernelsquare.global.common_response.error.code.CoffeeChatErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoffeeChatService {
    private final CoffeeChatRepository coffeeChatRepository;

    @Transactional
    public CreateCoffeeChatRoomResponse createCoffeeChatRoom(CreateCoffeeChatRoomRequest createCoffeeChatRoomRequest) {
        ChatRoom chatRoom = CreateCoffeeChatRoomRequest.toEntity(createCoffeeChatRoomRequest);

        ChatRoom saveChatRoom = coffeeChatRepository.save(chatRoom);

        return CreateCoffeeChatRoomResponse.from(saveChatRoom);
    }

    @Transactional
    public EnterCoffeeChatRoomResponse enterCoffeeChatRoom(EnterCoffeeChatRoomRequest enterCoffeeChatRoomRequest) {
        ChatRoom chatRoom = coffeeChatRepository.findById(enterCoffeeChatRoomRequest.roomId())
            .orElseThrow(() -> new BusinessException(CoffeeChatErrorCode.COFFEE_CHAT_ROOM_NOT_FOUND));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_MENTOR"))) {
            if (Long.valueOf(authentication.getName()).equals(enterCoffeeChatRoomRequest.memberId())) {

                chatRoom.activateRoom(enterCoffeeChatRoomRequest.articleTitle());

                return EnterCoffeeChatRoomResponse.of(enterCoffeeChatRoomRequest.articleTitle(), chatRoom);
            } else {
                throw new BusinessException(CoffeeChatErrorCode.MENTOR_MISMATCH);
            }
        } else if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))) {
            if (Boolean.FALSE.equals(chatRoom.getActive())) {
                throw new BusinessException(CoffeeChatErrorCode.COFFEE_CHAT_ROOM_NOT_ACTIVE);

            } else if (Long.valueOf(authentication.getName()).equals(enterCoffeeChatRoomRequest.memberId())) {
                return EnterCoffeeChatRoomResponse.of(enterCoffeeChatRoomRequest.articleTitle(), chatRoom);

            } else {
                throw new BusinessException(CoffeeChatErrorCode.MEMBER_MISMATCH);
            }

        } else {
            throw new BusinessException(CoffeeChatErrorCode.AUTHORITY_NOT_VALID);
        }
    }

    @Transactional
    public void leaveCoffeeChatRoom(String roomKey) {
        ChatRoom chatRoom = coffeeChatRepository.findByRoomKey(roomKey)
            .orElseThrow(() -> new BusinessException(CoffeeChatErrorCode.COFFEE_CHAT_ROOM_NOT_FOUND));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_MENTOR"))) {
            chatRoom.deactivateRoom();
        }
    }

    @Transactional
    @Scheduled(cron = "0 0/30 * * * *")
    public void disableRoom() {
        List<ChatRoom> chatRooms = coffeeChatRepository.findAllByActive(true);
        chatRooms.forEach(ChatRoom::deactivateRoom);
    }
}
