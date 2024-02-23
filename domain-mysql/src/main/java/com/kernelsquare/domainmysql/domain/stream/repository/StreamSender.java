package com.kernelsquare.domainmysql.domain.stream.repository;

import com.kernelsquare.domainmysql.domain.stream.entity.KafkaMessage;

public interface StreamSender {
    void sendKafkaMessage(KafkaMessage initKafkaMessage);
}
