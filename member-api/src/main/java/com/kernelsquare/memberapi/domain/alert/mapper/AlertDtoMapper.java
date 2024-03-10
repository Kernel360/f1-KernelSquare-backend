package com.kernelsquare.memberapi.domain.alert.mapper;

import com.kernelsquare.domainmongodb.domain.alert.command.AlertCommand;
import com.kernelsquare.domainmongodb.domain.alert.info.AlertInfo;
import com.kernelsquare.domainmysql.domain.answer.info.AnswerInfo;
import com.kernelsquare.domainmysql.domain.coffeechat.info.CoffeeChatInfo;
import com.kernelsquare.memberapi.domain.alert.dto.AlertDto;
import com.kernelsquare.memberapi.domain.alert.dto.CoffeeChatRequestAlertMessage;
import com.kernelsquare.memberapi.domain.alert.dto.QuestionReplyAlertMessage;
import com.kernelsquare.memberapi.domain.alert.dto.RankAnswerAlertMessage;
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

    QuestionReplyAlertMessage from(AnswerInfo answerInfo);

    RankAnswerAlertMessage of(AlertDto.RankAnswerAlert rankAnswerAlert);

    CoffeeChatRequestAlertMessage from(CoffeeChatInfo coffeeChatInfo);

    AlertDto.FindAllResponse toFindAllResponse(AlertInfo alertInfo);
}
