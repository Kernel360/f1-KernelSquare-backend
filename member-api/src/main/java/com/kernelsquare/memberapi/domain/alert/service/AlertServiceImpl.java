package com.kernelsquare.memberapi.domain.alert.service;

import com.kernelsquare.domainmongodb.domain.alert.entity.Alert;
import com.kernelsquare.memberapi.domain.alert.dto.AlertMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Async
    @Override
    public void sendToBroker(AlertMessage alertMessage) {
        Alert alert = alertMessage.process();

        kafkaTemplate.send("alert", alert);
    }
}
