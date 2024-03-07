package com.kernelsquare.memberapi.domain.chatgpt.client;

import java.net.URI;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.kernelsquare.memberapi.common.config.FeignConfig;
import com.kernelsquare.memberapi.domain.chatgpt.dto.ChatGptDto;

@FeignClient(name = "chatGptClient", configuration = FeignConfig.class)
public interface ChatGptClient {
	@PostMapping(produces = "application/json")
	ChatGptDto.CreateAnswerResponse getChatGptResponse(URI baseURL,
		@RequestHeader("Authorization") String apiKey,
		@RequestBody ChatGptDto.CreateAnswerRequest request
	);
}
