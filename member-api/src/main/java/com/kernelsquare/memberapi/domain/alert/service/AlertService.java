package com.kernelsquare.memberapi.domain.alert.service;

import com.kernelsquare.domainmongodb.domain.alert.command.AlertCommand;
import com.kernelsquare.domainmongodb.domain.alert.info.AlertInfo;
import com.kernelsquare.memberapi.domain.alert.dto.AlertMessage;

import java.util.List;

public interface AlertService {
    List<AlertInfo> findAllAlerts(AlertCommand.FindCommand command);

    void storeAndSendAlert(AlertMessage alterMessage);
}
