package com.kernelsquare.domainmysql.domain.stream.info;

import com.kernelsquare.domainmysql.domain.stream.entity.KafkaMessage;
import lombok.Builder;
import lombok.Getter;

@Getter
public class KafkaMessageInfo {
    private final String topic;
    private final Object message;
    private final KafkaMessage.TopicPrefix topicPrefix;

    @Builder
    public KafkaMessageInfo(KafkaMessage kafkaMessage) {
        this.topic = kafkaMessage.getTopic();
        this.message = kafkaMessage.getMessage();
        this.topicPrefix = kafkaMessage.getTopicPrefix();
    }

    public static KafkaMessageInfo of(KafkaMessage kafkaMessage) {
        return KafkaMessageInfo.builder()
            .kafkaMessage(kafkaMessage)
            .build();
    }
}
