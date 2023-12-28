package com.kernel360.kernelsquare.domain.tech_stack.dto;

import com.kernel360.kernelsquare.domain.tech_stack.entity.TechStack;

public record CreateTechStackResponse(
    Long skillId
) {
    public static CreateTechStackResponse of(TechStack techStack) {
        return new CreateTechStackResponse(
            techStack.getId()
        );
    }
}
