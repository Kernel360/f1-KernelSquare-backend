package com.kernelsquare.memberapi.domain.answer.mapper;

import com.kernelsquare.domainmysql.domain.answer.command.AnswerCommand;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.memberapi.domain.answer.dto.AnswerDto;
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
public interface AnswerDtoMapper {
    @Mapping(target = "author", expression = "java(mapMember(memberAdapter))")
    AnswerCommand.CreateAnswer toCommand(AnswerDto.CreateRequest request, Long questionId, MemberAdapter memberAdapter);

    default Member mapMember(MemberAdapter memberAdapter) {
        return memberAdapter.getMember();
    }
}