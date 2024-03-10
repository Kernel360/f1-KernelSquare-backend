package com.kernelsquare.memberapi.domain.alert.dto;

import com.kernelsquare.domainmongodb.domain.alert.entity.Alert;

public interface AlertMessage {
    Alert process();
}
