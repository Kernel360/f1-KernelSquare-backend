package com.kernelsquare.adminapi.domain.notice.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.kernelsquare.adminapi.domain.notice.dto.NoticeDto;
import com.kernelsquare.domainmysql.domain.notice.command.NoticeCommand;
import com.kernelsquare.domainmysql.domain.notice.info.NoticeInfo;

@Mapper(
	componentModel = "spring",
	injectionStrategy = InjectionStrategy.CONSTRUCTOR,
	unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface NoticeDtoMapper {
	NoticeCommand.CreateCommand toCommand(NoticeDto.CreateRequest request);

	NoticeCommand.UpdateCommand toCommand(NoticeDto.UpdateRequest request);

	NoticeDto.FindResponse toSingleResponse(NoticeInfo noticeInfo);

	NoticeDto.FindAllResponse toFindAllResponse(NoticeInfo noticeInfo);
}
