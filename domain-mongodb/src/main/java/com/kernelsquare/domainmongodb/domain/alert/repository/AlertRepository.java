package com.kernelsquare.domainmongodb.domain.alert.repository;

import com.kernelsquare.domainmongodb.domain.alert.entity.Alert;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AlertRepository extends MongoRepository<Alert, String> {
    List<Alert> findAllByRecipientId(String recipientId);
}
