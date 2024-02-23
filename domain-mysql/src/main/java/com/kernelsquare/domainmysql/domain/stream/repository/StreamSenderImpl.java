package com.kernelsquare.domainmysql.domain.stream.repository;

import com.kernelsquare.domainmysql.domain.stream.entity.KafkaMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StreamSenderImpl implements StreamSender {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendKafkaMessage(KafkaMessage initKafkaMessage) {

        log.info("토픽 : " + initKafkaMessage.getTopicPrefix().getDescription()+initKafkaMessage.getTopic() + ", message : " + initKafkaMessage.getMessage());
        kafkaTemplate.send(initKafkaMessage.getTopicPrefix().getDescription()+initKafkaMessage.getTopic(),
            initKafkaMessage.getMessage());
    }
}
