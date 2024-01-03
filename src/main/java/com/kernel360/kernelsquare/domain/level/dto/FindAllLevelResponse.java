package com.kernel360.kernelsquare.domain.level.dto;

import com.kernel360.kernelsquare.domain.level.entity.Level;

import java.util.List;

public record FindAllLevelResponse(
        List<LevelDto> levels
) {

    public static FindAllLevelResponse from(List<Level> levels) {
        return new FindAllLevelResponse(
                levels.stream().map(LevelDto::from).toList()
        );
    }
}
