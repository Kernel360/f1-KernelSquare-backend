package com.kernel360.kernelsquare.domain.tech_stack.dto;

import java.util.List;

public record FindAllTechStacksResponse(
    List<String> skills
) {
    public static FindAllTechStacksResponse from(List<String> skills) {
        return new FindAllTechStacksResponse(
            skills
        );
    }
}
