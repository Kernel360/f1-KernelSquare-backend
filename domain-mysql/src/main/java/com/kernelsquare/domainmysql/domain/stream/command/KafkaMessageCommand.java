package com.kernelsquare.domainmysql.domain.stream.command;

import com.kernelsquare.domainmysql.domain.stream.entity.KafkaMessage;
import lombok.Builder;

public class KafkaMessageCommand {
    @Builder
    public record PublishCommand(
        String topic,
        Object message,
        KafkaMessage.TopicPrefix topicPrefix
    ) {
        public KafkaMessage toKafkaMessage() {
            return KafkaMessage.builder()
                .topic(topic)
                .message(message)
                .topicPrefix(topicPrefix)
                .build();
        }
    }
}
