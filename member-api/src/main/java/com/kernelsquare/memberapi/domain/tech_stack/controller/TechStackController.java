package com.kernelsquare.memberapi.domain.tech_stack.controller;

import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;
import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.memberapi.domain.tech_stack.service.TechStackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kernelsquare.core.common_response.response.code.TechStackResponseCode.TECH_STACK_ALL_FOUND;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TechStackController {
	private final TechStackService techStackService;

	@GetMapping("/techs")
	public ResponseEntity<ApiResponse<PageResponse<String>>> findAllTechStacks(
		@PageableDefault(page = 0, size = 10)
		Pageable pageable
	) {
		PageResponse<String> pageResponse = techStackService.findAllTechStacks(pageable);

		return ResponseEntityFactory.toResponseEntity(TECH_STACK_ALL_FOUND, pageResponse);
	}
}
