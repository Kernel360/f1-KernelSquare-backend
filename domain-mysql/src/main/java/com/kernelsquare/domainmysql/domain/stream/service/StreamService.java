package com.kernelsquare.domainmysql.domain.stream.service;

import com.kernelsquare.domainmysql.domain.stream.command.KafkaMessageCommand;

public interface StreamService {
    void publishKafkaMessage(KafkaMessageCommand.PublishCommand command);
}
