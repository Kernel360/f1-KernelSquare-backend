package com.kernelsquare.memberapi.domain.alert.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kernelsquare.domainmongodb.domain.alert.entity.Alert;
import lombok.Builder;

import java.time.LocalDateTime;

public class AlertDto {
    @Builder
    public record FindAllResponse(
        String recipientId,
        String senderId,
        String message,
        Alert.AlertType alertType,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        LocalDateTime sendTime
    ) {}
}
