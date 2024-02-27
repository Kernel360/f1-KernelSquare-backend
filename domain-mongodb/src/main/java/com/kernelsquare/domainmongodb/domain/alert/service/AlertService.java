package com.kernelsquare.domainmongodb.domain.alert.service;

import com.kernelsquare.domainmongodb.domain.alert.command.AlertCommand;
import com.kernelsquare.domainmongodb.domain.alert.info.AlertInfo;

import java.util.List;

public interface AlertService {
    void createAlert(AlertCommand.CreateCommand command);

    List<AlertInfo> findAllAlerts(AlertCommand.FindCommand command);
}
