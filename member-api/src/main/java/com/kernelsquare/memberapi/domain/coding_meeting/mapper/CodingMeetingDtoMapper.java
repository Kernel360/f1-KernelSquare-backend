package com.kernelsquare.memberapi.domain.coding_meeting.mapper;

import com.kernelsquare.domainmysql.domain.coding_meeting.command.CodingMeetingCommand;
import com.kernelsquare.domainmysql.domain.coding_meeting.info.CodingMeetingInfo;
import com.kernelsquare.memberapi.domain.coding_meeting.dto.CodingMeetingDto;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface CodingMeetingDtoMapper {
    CodingMeetingDto.FindResponse toFindResponse(CodingMeetingInfo.Info codingMeetingInfo);
    CodingMeetingDto.FindAllResponse toFindAllResponse(CodingMeetingInfo.ListInfo codingMeetingListInfo);
    CodingMeetingDto.CreateResponse toCreateResponse(CodingMeetingInfo.TokenInfo codingMeetingTokenInfo);
    CodingMeetingCommand.CreateCommand toCreateCommand(CodingMeetingDto.CreateRequest request);
    CodingMeetingCommand.UpdateCommand toUpdateCommand(CodingMeetingDto.UpdateRequest request);
}
