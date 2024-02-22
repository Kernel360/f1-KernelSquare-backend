package com.kernelsquare.memberapi.domain.notice.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.kernelsquare.domainmysql.domain.notice.info.NoticeInfo;
import com.kernelsquare.memberapi.domain.notice.dto.NoticeDto;

@Mapper(
	componentModel = "spring",
	injectionStrategy = InjectionStrategy.CONSTRUCTOR,
	unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface NoticeDtoMapper {

	NoticeDto.FindResponse toSingleResponse(NoticeInfo noticeInfo);

	NoticeDto.FindAllResponse toFindAllResponse(NoticeInfo noticeInfo);
}
