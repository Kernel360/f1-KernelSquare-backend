package com.kernelsquare.memberapi.domain.coding_meeting.mapper;

import com.kernelsquare.domainmysql.domain.coding_meeting.command.CodingMeetingCommand;
import com.kernelsquare.domainmysql.domain.coding_meeting.info.CodingMeetingInfo;
import com.kernelsquare.domainmysql.domain.coding_meeting.info.CodingMeetingListInfo;
import com.kernelsquare.domainmysql.domain.coding_meeting.info.CodingMeetingTokenInfo;
import com.kernelsquare.memberapi.domain.coding_meeting.dto.CodingMeetingDto;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface CodingMeetingDtoMapper {
    CodingMeetingDto.FindResponse toSingleResponse(CodingMeetingInfo codingMeetingInfo);
    CodingMeetingDto.FindAllResponse toFindAllResponse(CodingMeetingListInfo codingMeetingListInfo);
    CodingMeetingDto.CreateResponse toCreateResponse(CodingMeetingTokenInfo codingMeetingTokenInfo);
    CodingMeetingCommand.CreateCommand toCreateCommand(CodingMeetingDto.CreateRequest request);
    CodingMeetingCommand.UpdateCommand toUpdateCommand(CodingMeetingDto.UpdateRequest request);
}
