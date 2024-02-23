package com.kernelsquare.memberapi.domain.stream.dto;

import com.kernelsquare.domainmysql.domain.stream.entity.KafkaMessage;
import lombok.Builder;

public class StreamDto {
    @Builder
    public record PublishRequest(
        String topic,
        Object message,
        KafkaMessage.TopicPrefix topicPrefix
    ) {}

    @Builder
    public record PublishResponse(
        String topic,
        Object message,
        KafkaMessage.TopicPrefix topicPrefix
    ) {}
}
