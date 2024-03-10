package com.kernelsquare.memberapi.domain.coffeechat.controller;

import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.memberapi.domain.coffeechat.dto.*;
import com.kernelsquare.memberapi.domain.coffeechat.facade.CoffeeChatFacade;
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
	private final CoffeeChatFacade coffeeChatFacade;

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
		@AuthenticationPrincipal
		MemberAdapter memberAdapter,
		@Valid
		@RequestBody
		EnterCoffeeChatRoomRequest enterCoffeeChatRoomRequest
	) {
		EnterCoffeeChatRoomResponse response = coffeeChatService.enterCoffeeChatRoom(enterCoffeeChatRoomRequest, memberAdapter);

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

	@PostMapping("/coffeechat/request/{memberId}")
	public ResponseEntity<ApiResponse> requestCoffeeChat(
		@AuthenticationPrincipal
		MemberAdapter memberAdapter,
		@PathVariable
		Long memberId
	) {
		coffeeChatFacade.sendCoffeeChatRequest(memberAdapter, memberId);

		return ResponseEntityFactory.toResponseEntity(COFFEE_CHAT_REQUEST_FINISHED);
	}
}
