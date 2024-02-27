package com.kernelsquare.memberapi.domain.alert.mapper;

import com.kernelsquare.domainmongodb.domain.alert.command.AlertCommand;
import com.kernelsquare.domainmongodb.domain.alert.info.AlertInfo;
import com.kernelsquare.memberapi.domain.alert.dto.AlertDto;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AlertDtoMapper {
    @Mapping(target = "memberId", expression = "java(mapMemberId(memberAdapter))")
    AlertCommand.FindCommand toCommand(MemberAdapter memberAdapter);

    default String mapMemberId(MemberAdapter memberAdapter) {
        return memberAdapter.getMember().getId().toString();
    }

    @Mapping(target = "recipientId", source = "memberId")
    AlertDto.FindAllResponse toFindAllResponse(AlertInfo alertInfo);
}
