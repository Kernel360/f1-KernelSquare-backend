package com.kernelsquare.alertapi.domain.alert.service;

import com.kernelsquare.alertapi.domain.alert.manager.SseManager;
import com.kernelsquare.domainmongodb.domain.alert.command.AlertCommand;
import com.kernelsquare.domainmongodb.domain.alert.entity.Alert;
import com.kernelsquare.domainmongodb.domain.alert.info.AlertInfo;
import com.kernelsquare.domainmongodb.domain.alert.repository.AlertReader;
import com.kernelsquare.domainmongodb.domain.alert.repository.AlertStore;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {
    private final AlertStore alertStore;
    private final AlertReader alertReader;
    private final SseManager sseManager;


    @Override
    public List<AlertInfo> findAllAlerts(AlertCommand.FindCommand command) {
        return alertReader.findAllAlerts(command.memberId()).stream()
            .map(AlertInfo::of)
            .toList();
    }

    @Override
    @KafkaListener(topics = "alert", groupId = "alert", containerFactory = "alertKafkaListenerContainerFactory")
    public void sendToClient(Alert alert) {
        sseManager.send(alert);
    }

    @Override
    @KafkaListener(topics = "alert", groupId = "mongo", containerFactory = "mongoKafkaListenerContainerFactory")
    public void sendToStorage(Alert alert) {
        alertStore.store(alert);
    }
}
