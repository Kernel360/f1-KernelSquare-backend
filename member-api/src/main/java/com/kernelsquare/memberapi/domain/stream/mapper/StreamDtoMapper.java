package com.kernelsquare.memberapi.domain.stream.mapper;

import com.kernelsquare.domainmysql.domain.stream.command.KafkaMessageCommand;
import com.kernelsquare.domainmysql.domain.stream.info.KafkaMessageInfo;
import com.kernelsquare.memberapi.domain.stream.dto.StreamDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface StreamDtoMapper {
    KafkaMessageCommand.PublishCommand toCommand(StreamDto.PublishRequest request);

    StreamDto.PublishResponse toPublishResponse(KafkaMessageInfo messageInfo);
}
