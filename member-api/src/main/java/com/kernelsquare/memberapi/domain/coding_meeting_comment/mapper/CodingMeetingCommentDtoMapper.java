package com.kernelsquare.memberapi.domain.coding_meeting_comment.mapper;

import com.kernelsquare.domainmysql.domain.coding_meeting_comment.command.CodingMeetingCommentCommand;
import com.kernelsquare.domainmysql.domain.coding_meeting_comment.info.CodingMeetingCommentListInfo;
import com.kernelsquare.memberapi.domain.coding_meeting_comment.dto.CodingMeetingCommentDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface CodingMeetingCommentDtoMapper {
    CodingMeetingCommentDto.FindAllResponse toFindAllResponse(CodingMeetingCommentListInfo codingMeetingCommentListInfo);
    CodingMeetingCommentCommand.CreateCommand toCreateCommand(CodingMeetingCommentDto.CreateRequest request);
    CodingMeetingCommentCommand.UpdateCommand toUpdateCommand(CodingMeetingCommentDto.UpdateRequest request);
}
