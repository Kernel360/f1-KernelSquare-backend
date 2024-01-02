package com.kernel360.kernelsquare.domain.tech_stack.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateTechStackRequest (
    @NotBlank
    String skill
) {}
