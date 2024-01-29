package com.kernel360.kernelsquare.domain.level.dto;

import com.kernel360.kernelsquare.domain.image.utils.ImageUtils;
import com.kernel360.kernelsquare.domain.level.entity.Level;

public record LevelDto(
        Long id,
        Long name,
        String imageUrl,
        Long leveUpperLimit
) {
    public static LevelDto from(Level level) {
        return new LevelDto(
                level.getId(),
                level.getName(),
                ImageUtils.makeImageUrl(level.getImageUrl()),
                level.getLevelUpperLimit()
        );
    }



}