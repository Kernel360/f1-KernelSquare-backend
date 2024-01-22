package com.kernel360.kernelsquare.domain.coffeechat.controller;

import com.kernel360.kernelsquare.domain.coffeechat.dto.CreateCoffeeChatRoomRequest;
import com.kernel360.kernelsquare.domain.coffeechat.dto.CreateCoffeeChatRoomResponse;
import com.kernel360.kernelsquare.domain.coffeechat.dto.EnterCoffeeChatRoomRequest;
import com.kernel360.kernelsquare.domain.coffeechat.dto.EnterCoffeeChatRoomResponse;
import com.kernel360.kernelsquare.domain.coffeechat.service.CoffeeChatService;
import com.kernel360.kernelsquare.global.common_response.ApiResponse;
import com.kernel360.kernelsquare.global.common_response.ResponseEntityFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.kernel360.kernelsquare.global.common_response.response.code.CoffeeChatResponseCode.*;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CoffeeChatController {
    private final CoffeeChatService coffeeChatService;

    @PostMapping("/coffeechat/rooms")
    public ResponseEntity<ApiResponse<CreateCoffeeChatRoomResponse>> createCoffeeChatRoom(
        @Valid
        @RequestBody
        CreateCoffeeChatRoomRequest createCoffeeChatRoomRequest
    ) {
        CreateCoffeeChatRoomResponse response = coffeeChatService.createCoffeeChatRoom(createCoffeeChatRoomRequest);

        return ResponseEntityFactory.toResponseEntity(COFFEE_CHAT_ROOM_CREATED, response);
    }

    @PostMapping("/coffeechat/rooms/enter")
    public ResponseEntity<ApiResponse<EnterCoffeeChatRoomResponse>> enterCoffeeChatRoom(
        @Valid
        @RequestBody
        EnterCoffeeChatRoomRequest enterCoffeeChatRoomRequest
    ) {
        EnterCoffeeChatRoomResponse response = coffeeChatService.enterCoffeeChatRoom(enterCoffeeChatRoomRequest);

        return ResponseEntityFactory.toResponseEntity(ROOM_ENTRY_SUCCESSFUL, response);
    }

    @PostMapping("/coffeechat/rooms/{roomKey}")
    public ResponseEntity<ApiResponse> leaveCoffeeChatRoom(
        @Valid
        @PathVariable
        String roomKey
    ) {
        coffeeChatService.leaveCoffeeChatRoom(roomKey);

        return ResponseEntityFactory.toResponseEntity(COFFEE_CHAT_ROOM_LEAVE);
    }
}
