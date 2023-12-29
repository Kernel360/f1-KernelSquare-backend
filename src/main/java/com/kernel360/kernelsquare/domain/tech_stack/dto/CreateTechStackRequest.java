package com.kernel360.kernelsquare.domain.tech_stack.dto;

import com.kernel360.kernelsquare.domain.tech_stack.entity.TechStack;
import jakarta.validation.constraints.NotBlank;

public record CreateTechStackRequest(
    @NotBlank
    String skill
) {
    public static TechStack toEntity(CreateTechStackRequest createTechStackRequest) {
        return TechStack.builder()
            .skill(createTechStackRequest.skill())
            .build();
    }
}
