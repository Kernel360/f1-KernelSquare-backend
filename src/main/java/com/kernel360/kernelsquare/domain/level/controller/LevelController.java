package com.kernel360.kernelsquare.domain.level.controller;

import com.kernel360.kernelsquare.domain.level.dto.*;
import com.kernel360.kernelsquare.domain.level.service.LevelService;
import com.kernel360.kernelsquare.global.common_response.ApiResponse;
import com.kernel360.kernelsquare.global.common_response.ResponseEntityFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.kernel360.kernelsquare.global.common_response.response.code.LevelResponseCode.LEVEL_CREATED;
import static com.kernel360.kernelsquare.global.common_response.response.code.LevelResponseCode.LEVEL_FOUND;

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

        return ResponseEntityFactory.toResponseEntity(LEVEL_FOUND,response);
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
