package com.kernelsquare.memberapi.domain.coffeechat.mapper;

import com.kernelsquare.domainmysql.domain.coffeechat.command.CoffeeChatCommand;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface CoffeeChatDtoMapper {

    @Mapping(target = "sender", expression = "java(mapMember(memberAdapter))")
    @Mapping(target = "recipientId", source = "memberId")
    CoffeeChatCommand.RequestCommand toCommand(MemberAdapter memberAdapter, Long memberId);

    default Member mapMember(MemberAdapter memberAdapter) {
        return memberAdapter.getMember();
    }
}
