package com.kernelsquare.memberapi.domain.alert.service;

import com.kernelsquare.domainmongodb.domain.alert.entity.Alert;
import com.kernelsquare.domainmongodb.domain.alert.info.AlertInfo;
import com.kernelsquare.domainmongodb.domain.alert.repository.AlertReader;
import com.kernelsquare.domainmongodb.domain.alert.repository.AlertStore;
import com.kernelsquare.domainmongodb.domain.alert.command.AlertCommand;
import com.kernelsquare.memberapi.domain.alert.dto.AlertMessage;
import com.kernelsquare.memberapi.domain.alert.manager.SseManager;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Async
    @Override
    public void storeAndSendAlert(AlertMessage alertMessage) {
        Alert alert = alertMessage.process();

        alertStore.store(alert);

        sseManager.send(alert);
    }
}
