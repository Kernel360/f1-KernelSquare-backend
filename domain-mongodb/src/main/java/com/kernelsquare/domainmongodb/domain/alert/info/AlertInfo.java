package com.kernelsquare.domainmongodb.domain.alert.info;

import com.kernelsquare.domainmongodb.domain.alert.entity.Alert;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AlertInfo {
    private final String memberId;
    private final String message;
    private final Alert.AlertType alertType;
    private final LocalDateTime sendTime;

    @Builder
    public AlertInfo(Alert alert) {
        this.memberId = alert.getMemberId();
        this.message = alert.getMessage();
        this.alertType = alert.getAlertType();
        this.sendTime = alert.getSendTime();
    }

    public static AlertInfo of(Alert alert) {
        return AlertInfo.builder()
            .alert(alert)
            .build();
    }
}
