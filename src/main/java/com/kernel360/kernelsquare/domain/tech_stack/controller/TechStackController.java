package com.kernel360.kernelsquare.domain.tech_stack.controller;

import com.kernel360.kernelsquare.domain.tech_stack.dto.CreateTechStackRequest;
import com.kernel360.kernelsquare.domain.tech_stack.dto.CreateTechStackResponse;
import com.kernel360.kernelsquare.domain.tech_stack.service.TechStackService;
import com.kernel360.kernelsquare.global.common_response.ApiResponse;
import com.kernel360.kernelsquare.global.common_response.ResponseEntityFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kernel360.kernelsquare.global.common_response.response.code.TechStackResponseCode.*;

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
        CreateTechStackResponse response = techStackService.createTechStack(createTechStackRequest);

        return ResponseEntityFactory.toResponseEntity(TECH_STACK_CREATED, response);
    }
}
