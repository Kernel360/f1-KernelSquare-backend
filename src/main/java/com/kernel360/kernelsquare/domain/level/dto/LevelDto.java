package com.kernel360.kernelsquare.domain.level.dto;

import com.kernel360.kernelsquare.domain.level.entity.Level;

public record LevelDto(
        Long name,
        String imageUrl
) {
    public static LevelDto from(Level level) {
        return new LevelDto(
                level.getName(),
                level.getImageUrl()
        );
    }



}