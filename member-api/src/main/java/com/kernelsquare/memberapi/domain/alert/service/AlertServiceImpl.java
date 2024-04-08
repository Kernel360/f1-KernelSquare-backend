package com.kernelsquare.memberapi.domain.alert.service;

import com.kernelsquare.domainkafka.domain.alert.entity.Alert;
import com.kernelsquare.domainkafka.domain.alert.repository.AlertSender;
import com.kernelsquare.memberapi.domain.alert.dto.AlertMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {
    private final AlertSender alertSender;

    @Async
    @Override
    public void sendToBroker(AlertMessage alertMessage) {
        Alert alert = alertMessage.process();

        alertSender.send(alert);
    }
}
