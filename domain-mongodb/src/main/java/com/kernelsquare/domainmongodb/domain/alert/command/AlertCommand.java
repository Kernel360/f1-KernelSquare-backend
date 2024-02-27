package com.kernelsquare.domainmongodb.domain.alert.command;

import com.kernelsquare.domainmongodb.domain.alert.entity.Alert;
import lombok.Builder;

import java.time.LocalDateTime;

public class AlertCommand {
    @Builder
    public record CreateCommand(
        String memberId,
        String message,
        Alert.AlertType alertType,
        LocalDateTime sendTime
    ) {
        public Alert toEntity() {
            return Alert.builder()
                .memberId(memberId)
                .message(message)
                .alertType(alertType)
                .build();
        }
    }

    @Builder
    public record FindCommand(
        String memberId
    ) {}
}
