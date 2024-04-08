package com.kernelsquare.domainkafka.domain.alert.repository;

import com.kernelsquare.domainkafka.domain.alert.entity.Alert;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.kernelsquare.domainkafka.constants.KafkaConstants.ALERT_TOPIC;

@Component
@RequiredArgsConstructor
public class AlertSenderImpl implements AlertSender {
    private final KafkaTemplate<String, Alert> kafkaTemplate;

    @Override
    public void send(Alert alert) {
        kafkaTemplate.send(ALERT_TOPIC, alert);
    }
}
