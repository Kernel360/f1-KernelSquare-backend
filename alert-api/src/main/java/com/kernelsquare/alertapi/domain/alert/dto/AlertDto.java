package com.kernelsquare.alertapi.domain.alert.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kernelsquare.core.constants.TimeResponseFormat;
import com.kernelsquare.domainmongodb.domain.alert.entity.Alert;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class AlertDto {
    @Builder
    public record FindAllResponse(
        String recipientId,
        String recipient,
        String senderId,
        String sender,
        String message,
        Alert.AlertType alertType,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
        LocalDateTime sendTime
    ) {}

    @Builder
    public record PersonalFindAllResponse(
        List<FindAllResponse> personalAlertList
    ) {
        public static PersonalFindAllResponse from(List<FindAllResponse> personalAlert) {
            return new PersonalFindAllResponse(
                personalAlert
            );
        }
    }
}
