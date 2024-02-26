package com.kernelsquare.memberapi.domain.coffeechat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ScreenController {
	@GetMapping("/chat/{roomId}/{sender}")
	public String roomDetail(
		Model model,
		@PathVariable
		String roomId,
		@PathVariable
		String sender) {
		model.addAttribute("roomId", roomId);
		model.addAttribute("sender", sender);
		return "chatscreen";
	}

	@GetMapping("/sse/{memberId}")
	public String sseDetail(
		Model model,
		@PathVariable
		String memberId) {
		model.addAttribute("memberId", memberId);
		return "ssescreen";
	}
}
