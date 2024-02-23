package com.kernelsquare.domainmysql.domain.stream.service;

import com.kernelsquare.domainmysql.domain.stream.command.KafkaMessageCommand;
import com.kernelsquare.domainmysql.domain.stream.repository.StreamSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StreamServiceImpl implements StreamService {
    private final StreamSender streamSender;

    @Override
    public void publishKafkaMessage(KafkaMessageCommand.PublishCommand command) {
        var initKafkaMessage = command.toKafkaMessage();
        streamSender.sendKafkaMessage(initKafkaMessage);
    }
}
