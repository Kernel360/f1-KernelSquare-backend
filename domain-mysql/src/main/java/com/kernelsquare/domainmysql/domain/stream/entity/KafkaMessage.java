package com.kernelsquare.domainmysql.domain.stream.entity;

import com.kernelsquare.core.common_response.error.exception.InvalidParamException;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Getter
public class KafkaMessage {
    private String topic;
    private Object message;
    private TopicPrefix topicPrefix;

    @Getter
    @RequiredArgsConstructor
    public enum TopicPrefix {
        CHATTING("chat_");

        private final String description;
    }

    @Builder
    public KafkaMessage(String topic, Object message, TopicPrefix topicPrefix) {
        if (StringUtils.isBlank(topic))
            throw new InvalidParamException("Invalid topic");
        if (Objects.isNull(message))
            throw new InvalidParamException("Invalid message");
        if (Objects.isNull(topicPrefix))
            throw new InvalidParamException("Invalid topic prefix");

        this.topic = topic;
        this.message = message;
        this.topicPrefix = topicPrefix;
    }
}
