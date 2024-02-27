package com.kernelsquare.domainmysql.domain.stream.repository;

import com.kernelsquare.domainmysql.domain.stream.entity.KafkaMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StreamSenderImpl implements StreamSender {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendKafkaMessage(KafkaMessage initKafkaMessage) {
        kafkaTemplate.send(initKafkaMessage.getTopicPrefix().getDescription()+initKafkaMessage.getTopic(),
            initKafkaMessage.getMessage());
    }
}
