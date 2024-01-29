package com.kernel360.kernelsquare.domain.tech_stack.dto;

import com.kernel360.kernelsquare.domain.tech_stack.entity.TechStack;

import java.util.List;

public record FindAllTechStacksResponse(
    List<TechStack> skills
) {
    public static FindAllTechStacksResponse from(List<TechStack> skills) {
        return new FindAllTechStacksResponse(
            skills
        );
    }
}
