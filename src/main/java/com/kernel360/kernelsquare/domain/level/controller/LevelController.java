package com.kernel360.kernelsquare.domain.level.controller;

import com.kernel360.kernelsquare.domain.level.dto.CreateLevelRequest;
import com.kernel360.kernelsquare.domain.level.dto.CreateLevelResponse;
import com.kernel360.kernelsquare.domain.level.dto.FindAllLevelResponse;
import com.kernel360.kernelsquare.domain.level.service.LevelService;
import com.kernel360.kernelsquare.global.common_response.ApiResponse;
import com.kernel360.kernelsquare.global.common_response.ResponseEntityFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.kernel360.kernelsquare.global.common_response.response.code.LevelResponseCode.*;

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


    @DeleteMapping("/levels/{levelId}")
    public ResponseEntity<ApiResponse> deleteLevel(
            @PathVariable
            Long levelId
    ) {
        levelService.deleteLevel(levelId);

        return ResponseEntityFactory.toResponseEntity(LEVEL_DELETED);
    }

}
