package com.kernel360.kernelsquare.domain.level.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kernel360.kernelsquare.domain.level.entity.Level;

import java.io.Serializable;

/**
 * DTO for {@link com.kernel360.kernelsquare.domain.level.entity.Level}
 */
public record UpdateLevelRequest(
        Long id,
        Long name,
        String imageUrl
) {

    public static Level toEntity(
            UpdateLevelRequest updateLevelRequest) {
        return Level.builder()
                .id(updateLevelRequest.id())
                .name(updateLevelRequest.name())
                .imageUrl(updateLevelRequest.imageUrl())
                .build();
    }
}