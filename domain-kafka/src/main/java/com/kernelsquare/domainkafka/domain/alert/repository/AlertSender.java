package com.kernelsquare.domainkafka.domain.alert.repository;

import com.kernelsquare.domainkafka.domain.alert.entity.Alert;

public interface AlertSender {
    void send(Alert alert);
}
