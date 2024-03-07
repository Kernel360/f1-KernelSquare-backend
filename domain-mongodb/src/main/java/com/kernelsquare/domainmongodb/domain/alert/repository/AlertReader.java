package com.kernelsquare.domainmongodb.domain.alert.repository;

import com.kernelsquare.domainmongodb.domain.alert.entity.Alert;

import java.util.List;

public interface AlertReader {
    List<Alert> findAllAlerts(String memberId);
}
