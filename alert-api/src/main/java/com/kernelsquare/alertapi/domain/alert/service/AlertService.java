package com.kernelsquare.alertapi.domain.alert.service;

import com.kernelsquare.domainmongodb.domain.alert.command.AlertCommand;
import com.kernelsquare.domainmongodb.domain.alert.entity.Alert;
import com.kernelsquare.domainmongodb.domain.alert.info.AlertInfo;

import java.util.List;

public interface AlertService {
    List<AlertInfo> findAllAlerts(AlertCommand.FindCommand command);

    void sendToClient(Alert alert);

    void sendToStorage(Alert alert);
}
