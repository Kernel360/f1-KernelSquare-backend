package com.kernel360.kernelsquare.domain.level.controller;

import com.kernel360.kernelsquare.domain.level.dto.CreateLevelRequest;
import com.kernel360.kernelsquare.domain.level.dto.CreateLevelResponse;
import com.kernel360.kernelsquare.domain.level.service.LevelService;
import com.kernel360.kernelsquare.global.common_response.ApiResponse;
import com.kernel360.kernelsquare.global.common_response.ResponseEntityFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kernel360.kernelsquare.global.common_response.response.code.LevelResponseCode.LEVEL_CREATED;

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

}
