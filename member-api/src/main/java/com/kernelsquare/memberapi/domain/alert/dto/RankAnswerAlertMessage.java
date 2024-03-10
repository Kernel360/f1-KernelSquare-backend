package com.kernelsquare.memberapi.domain.alert.dto;

import com.kernelsquare.core.constants.SystemConstants;
import com.kernelsquare.domainmongodb.domain.alert.entity.Alert;
import lombok.Builder;

@Builder
public class RankAnswerAlertMessage implements AlertMessage {
    private String recipientId;
    private String recipient;
    private String questionTitle;
    private String rankName;

    @Override
    public Alert process() {
        return Alert.builder()
            .recipientId(recipientId)
            .recipient(recipient)
            .senderId(SystemConstants.ALERT_SYSTEM)
            .sender(SystemConstants.ALERT_SYSTEM)
            .message(questionTitle + " 글에 작성하신 답변이 " + rankName + "등 답변이 되었습니다.")
            .alertType(Alert.AlertType.RANK_ANSWER)
            .build();
    }
}
