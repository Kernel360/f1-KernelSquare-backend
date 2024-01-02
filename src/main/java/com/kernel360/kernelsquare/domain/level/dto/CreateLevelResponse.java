package com.kernel360.kernelsquare.domain.level.dto;

import com.kernel360.kernelsquare.domain.level.entity.Level;
import com.kernel360.kernelsquare.domain.tech_stack.entity.TechStack;

import java.io.Serializable;
import java.time.LocalDateTime;


public record CreateLevelResponse(
        Long levelId
) {
    public static CreateLevelResponse of(Level level) {
        return new CreateLevelResponse(
                level.getId()
        );
    }

}