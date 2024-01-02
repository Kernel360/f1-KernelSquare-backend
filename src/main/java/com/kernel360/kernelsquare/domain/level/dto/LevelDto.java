package com.kernel360.kernelsquare.domain.level.dto;

import com.kernel360.kernelsquare.domain.level.entity.Level;

public record LevelDto(
        Long id,
        Long name,
        String imageUrl
) {
    public static LevelDto from(Level level) {
        return new LevelDto(
                level.getId(),
                level.getName(),
                level.getImageUrl()
        );
    }



}