package com.kernelsquare.memberapi.domain.alert.service;

import com.kernelsquare.memberapi.domain.alert.dto.AlertMessage;

public interface AlertService {
    void sendToBroker(AlertMessage alterMessage);
}
