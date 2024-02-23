package com.kernelsquare.memberapi.domain.stream.facade;

import com.kernelsquare.domainmysql.domain.stream.service.StreamService;
import com.kernelsquare.memberapi.domain.stream.dto.StreamDto;
import com.kernelsquare.memberapi.domain.stream.mapper.StreamDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StreamFacade {
    private final StreamService streamService;
    private final StreamDtoMapper streamDtoMapper;

    public void sendKafka(StreamDto.PublishRequest request) {
        streamService.publishKafkaMessage(streamDtoMapper.toCommand(request));
    }
}
