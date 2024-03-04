package com.kernelsquare.domainmongodb.domain.alert.repository;

import com.kernelsquare.domainmongodb.domain.alert.entity.Alert;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlertStoreImpl implements AlertStore {
    private final AlertRepository alertRepository;

    @Async
    @Override
    public void store(Alert alert) {
        alertRepository.save(alert);
    }
}
