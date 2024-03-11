package com.kernelsquare.adminapi.domain.member.mapper;

import com.kernelsquare.adminapi.domain.member.dto.MemberDto;
import com.kernelsquare.domainmysql.domain.member.command.MemberCommand;
import com.kernelsquare.domainmysql.domain.member.info.MemberInfo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MemberDtoMapper {
    MemberCommand.UpdateAuthorityCommand toCommand(MemberDto.UpdateAuthorityRequest request);

    MemberCommand.UpdateNicknameCommand toCommand(MemberDto.UpdateNicknameRequest request);

    @Mapping(target = "memberId", source = "id")
    MemberDto.FindResponse toFindResponse(MemberInfo memberInfo);
}
