package com.kernelsquare.domainmongodb.domain.alert.service;

import com.kernelsquare.domainmongodb.domain.alert.command.AlertCommand;
import com.kernelsquare.domainmongodb.domain.alert.info.AlertInfo;
import com.kernelsquare.domainmongodb.domain.alert.repository.AlertReader;
import com.kernelsquare.domainmongodb.domain.alert.repository.AlertStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {
    private final AlertStore alertStore;
    private final AlertReader alertReader;

//    @Override
//    public void createAlert(AlertCommand.CreateCommand command) {
//        var initAlert = command.toEntity();
//        alertStore.store(initAlert);
//    }

    @Override
    public List<AlertInfo> findAllAlerts(AlertCommand.FindCommand command) {
        return alertReader.findAllAlerts(command.memberId()).stream()
            .map(AlertInfo::of)
            .toList();
    }
}
