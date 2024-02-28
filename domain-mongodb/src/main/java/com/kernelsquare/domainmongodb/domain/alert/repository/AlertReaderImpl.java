package com.kernelsquare.domainmongodb.domain.alert.repository;

import com.kernelsquare.domainmongodb.domain.alert.entity.Alert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AlertReaderImpl implements AlertReader {
    private final AlertRepository alertRepository;

    @Override
    public List<Alert> findAllAlerts(String memberId) {
        return alertRepository.findAllByRecipientId(memberId);
    }
}
