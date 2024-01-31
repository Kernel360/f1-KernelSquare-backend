package com.kernelsquare.memberapi.domain.tech_stack.controller;

import static com.kernelsquare.core.common_response.response.code.TechStackResponseCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kernelsquare.memberapi.domain.tech_stack.dto.FindAllTechStacksResponse;
import com.kernelsquare.memberapi.domain.tech_stack.service.TechStackService;
import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TechStackController {
	private final TechStackService techStackService;

	@GetMapping("/techs")
	public ResponseEntity<ApiResponse<FindAllTechStacksResponse>> findAllTechStacks() {
		FindAllTechStacksResponse findAllTechStacksResponse = techStackService.findAllTechStacks();

		return ResponseEntityFactory.toResponseEntity(TECH_STACK_ALL_FOUND, findAllTechStacksResponse);
	}
}
