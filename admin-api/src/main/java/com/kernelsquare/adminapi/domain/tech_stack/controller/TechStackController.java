package com.kernelsquare.adminapi.domain.tech_stack.controller;

import static com.kernelsquare.core.common_response.response.code.TechStackResponseCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kernelsquare.adminapi.domain.tech_stack.dto.CreateTechStackRequest;
import com.kernelsquare.adminapi.domain.tech_stack.dto.CreateTechStackResponse;
import com.kernelsquare.adminapi.domain.tech_stack.dto.FindAllTechStacksResponse;
import com.kernelsquare.adminapi.domain.tech_stack.dto.UpdateTechStackRequest;
import com.kernelsquare.adminapi.domain.tech_stack.service.TechStackService;
import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TechStackController {
	private final TechStackService techStackService;

	@PostMapping("/techs")
	public ResponseEntity<ApiResponse<CreateTechStackResponse>> createTechStack(
		@Valid
		@RequestBody
		CreateTechStackRequest createTechStackRequest
	) {
		CreateTechStackResponse createTechStackResponse = techStackService.createTechStack(createTechStackRequest);

		return ResponseEntityFactory.toResponseEntity(TECH_STACK_CREATED, createTechStackResponse);
	}

	@GetMapping("/techs")
	public ResponseEntity<ApiResponse<FindAllTechStacksResponse>> findAllTechStacks() {
		FindAllTechStacksResponse findAllTechStacksResponse = techStackService.findAllTechStacks();

		return ResponseEntityFactory.toResponseEntity(TECH_STACK_ALL_FOUND, findAllTechStacksResponse);
	}

	@PutMapping("/techs/{techStackId}")
	public ResponseEntity<ApiResponse> updateTechStacks(
		@PathVariable
		Long techStackId,
		@Valid
		@RequestBody
		UpdateTechStackRequest updateTechStackRequest
	) {
		techStackService.updateTechStack(techStackId, updateTechStackRequest);

		return ResponseEntityFactory.toResponseEntity(TECH_STACK_UPDATED);
	}

	@DeleteMapping("/techs/{techStackId}")
	public ResponseEntity<ApiResponse> deleteTechStack(
		@PathVariable
		Long techStackId
	) {
		techStackService.deleteTechStack(techStackId);

		return ResponseEntityFactory.toResponseEntity(TECH_STACK_DELETED);
	}
}
