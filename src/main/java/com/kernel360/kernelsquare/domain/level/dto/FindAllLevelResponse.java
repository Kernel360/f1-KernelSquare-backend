package com.kernel360.kernelsquare.domain.level.dto;

import com.kernel360.kernelsquare.domain.level.entity.Level;

import java.util.List;

public record FindAllLevelResponse(
        List<Level> levels
) {

    public static FindAllLevelResponse from(List<Level> levels) {
        return new FindAllLevelResponse(
                levels
        );
    }
}
