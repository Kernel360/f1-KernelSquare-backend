package com.kernel360.kernelsquare.domain.level.dto;

import com.kernel360.kernelsquare.domain.level.entity.Level;
import org.springframework.web.bind.annotation.DeleteMapping;

public record DeleteLevelRequest(
        Long id
) {
    public static Level toEntity(
            DeleteLevelRequest deleteLevelRequest) {
        return Level.builder()
                .id(deleteLevelRequest.id())
                .build();
    }
}
