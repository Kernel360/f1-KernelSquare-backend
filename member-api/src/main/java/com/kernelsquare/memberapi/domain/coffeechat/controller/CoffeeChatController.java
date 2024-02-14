package com.kernelsquare.memberapi.domain.coffeechat.controller;

import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;
import com.kernelsquare.memberapi.domain.coffeechat.dto.*;
import com.kernelsquare.memberapi.domain.coffeechat.service.CoffeeChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.kernelsquare.core.common_response.response.code.CoffeeChatResponseCode.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CoffeeChatController {
	private final CoffeeChatService coffeeChatService;

	@PostMapping("/coffeechat/rooms")
	public ResponseEntity<ApiResponse<CreateCoffeeChatRoomResponse>> createCoffeeChatRoom(
		@Valid
		@RequestBody
			//
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

	@GetMapping("/coffeechat/rooms/{roomKey}")
	public ResponseEntity<ApiResponse<FindChatHistoryResponse>> findChatHistory(
		@Valid
		@PathVariable
		String roomKey
	) {
		FindChatHistoryResponse response = coffeeChatService.findChatHistory(roomKey);

		return ResponseEntityFactory.toResponseEntity(CHAT_HISTORY_FOUND, response);
	}
}
