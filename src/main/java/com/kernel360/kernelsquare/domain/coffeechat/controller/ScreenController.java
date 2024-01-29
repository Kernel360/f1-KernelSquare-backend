package com.kernel360.kernelsquare.domain.coffeechat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ScreenController {
    // 테스트용 채팅방 화면
    @GetMapping("/screen/{roomId}/{sender}")
    public String roomDetail(
        Model model,
        @PathVariable
        String roomId,
        @PathVariable
        String sender) {
        model.addAttribute("roomId", roomId);
        model.addAttribute("sender", sender);
        return "chat/screen";
    }
}
