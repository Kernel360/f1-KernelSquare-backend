package com.kernelsquare.memberapi.domain.alert.dto;

import com.kernelsquare.core.constants.SystemConstants;
import com.kernelsquare.domainkafka.domain.alert.entity.Alert;
import lombok.Builder;

import java.util.HashMap;
import java.util.Map;

@Builder
public class RankAnswerAlertMessage implements AlertMessage {
    private String recipientId;
    private String recipient;
    private String questionId;
    private String questionTitle;
    private String rank;

    @Override
    public Alert process() {
        Map<String, String> payload = new HashMap<>(Map.of(
            "questionId", questionId,
            "questionTitle", questionTitle,
            "rank", rank
        ));

        return Alert.builder()
            .recipientId(recipientId)
            .recipient(recipient)
            .senderId(SystemConstants.ALERT_SYSTEM)
            .sender(SystemConstants.ALERT_SYSTEM)
            .alertType(Alert.AlertType.RANK_ANSWER)
            .payload(payload)
            .build();
    }
}
