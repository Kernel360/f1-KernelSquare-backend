package com.kernelsquare.memberapi.domain.alert.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kernelsquare.domainmongodb.domain.alert.entity.Alert;
import com.kernelsquare.memberapi.domain.answer.dto.FindAllAnswerResponse;
import com.kernelsquare.memberapi.domain.answer.dto.FindAnswerResponse;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

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
