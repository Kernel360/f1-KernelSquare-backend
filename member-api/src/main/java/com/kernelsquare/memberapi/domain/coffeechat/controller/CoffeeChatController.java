package com.kernelsquare.memberapi.domain.coffeechat.controller;

import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.memberapi.domain.coffeechat.dto.*;
import com.kernelsquare.memberapi.domain.coffeechat.service.CoffeeChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.kernelsquare.core.common_response.response.code.CoffeeChatResponseCode.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CoffeeChatController {
	private final CoffeeChatService coffeeChatService;

	@PostMapping("/coffeechat/rooms/enter")
	public ResponseEntity<ApiResponse<EnterCoffeeChatRoomResponse>> enterCoffeeChatRoom(
		@AuthenticationPrincipal
		MemberAdapter memberAdapter,
		@Valid
		@RequestBody
		EnterCoffeeChatRoomRequest enterCoffeeChatRoomRequest
	) {
		EnterCoffeeChatRoomResponse response = coffeeChatService.enterCoffeeChatRoom(enterCoffeeChatRoomRequest, memberAdapter);

		return ResponseEntityFactory.toResponseEntity(ROOM_ENTRY_SUCCESSFUL, response);
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
