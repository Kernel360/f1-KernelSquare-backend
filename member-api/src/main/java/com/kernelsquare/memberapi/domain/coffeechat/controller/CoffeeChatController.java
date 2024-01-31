package com.kernelsquare.memberapi.domain.coffeechat.controller;

import static com.kernelsquare.core.common_response.response.code.CoffeeChatResponseCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kernelsquare.memberapi.domain.coffeechat.dto.CreateCoffeeChatRoomRequest;
import com.kernelsquare.memberapi.domain.coffeechat.dto.CreateCoffeeChatRoomResponse;
import com.kernelsquare.memberapi.domain.coffeechat.dto.EnterCoffeeChatRoomRequest;
import com.kernelsquare.memberapi.domain.coffeechat.dto.EnterCoffeeChatRoomResponse;
import com.kernelsquare.memberapi.domain.coffeechat.service.CoffeeChatService;
import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
}
