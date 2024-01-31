package com.kernelsquare.adminapi.domain.level.controller;

import static com.kernelsquare.core.common_response.response.code.LevelResponseCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kernelsquare.adminapi.domain.level.dto.CreateLevelRequest;
import com.kernelsquare.adminapi.domain.level.dto.CreateLevelResponse;
import com.kernelsquare.adminapi.domain.level.dto.FindAllLevelResponse;
import com.kernelsquare.adminapi.domain.level.dto.UpdateLevelRequest;
import com.kernelsquare.adminapi.domain.level.dto.UpdateLevelResponse;
import com.kernelsquare.adminapi.domain.level.service.LevelService;
import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LevelController {
	private final LevelService levelService;

	@PostMapping("/levels")
	public ResponseEntity<ApiResponse<CreateLevelResponse>> createLevel(
		@Valid
		@RequestBody
		CreateLevelRequest createLevelRequest
	) {
		CreateLevelResponse response = levelService.createLevel(createLevelRequest);

		return ResponseEntityFactory.toResponseEntity(LEVEL_CREATED, response);
	}

	@GetMapping("/levels")
	public ResponseEntity<ApiResponse<FindAllLevelResponse>> findAllLevel() {
		FindAllLevelResponse response = levelService.findAllLevel();

		return ResponseEntityFactory.toResponseEntity(LEVEL_FOUND, response);
	}

	@DeleteMapping("/levels/{levelId}")
	public ResponseEntity<ApiResponse> deleteLevel(
		@PathVariable
		Long levelId
	) {
		levelService.deleteLevel(levelId);

		return ResponseEntityFactory.toResponseEntity(LEVEL_DELETED);
	}

	@PutMapping("/levels/{levelId}")
	public ResponseEntity<ApiResponse<UpdateLevelResponse>> updateLevel(
		@PathVariable
		Long levelId,
		@RequestBody
		UpdateLevelRequest updateLevelRequest
	) {
		UpdateLevelResponse response = levelService.updateLevel(levelId, updateLevelRequest);

		return ResponseEntityFactory.toResponseEntity(LEVEL_UPDATED, response);
	}
}
