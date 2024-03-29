package com.kernelsquare.memberapi.domain.alert.mapper;

import com.kernelsquare.domainmysql.domain.answer.info.AnswerInfo;
import com.kernelsquare.domainmysql.domain.coffeechat.info.CoffeeChatInfo;
import com.kernelsquare.memberapi.domain.alert.dto.AlertDto;
import com.kernelsquare.memberapi.domain.alert.dto.CoffeeChatRequestAlertMessage;
import com.kernelsquare.memberapi.domain.alert.dto.QuestionReplyAlertMessage;
import com.kernelsquare.memberapi.domain.alert.dto.RankAnswerAlertMessage;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AlertDtoMapper {
    QuestionReplyAlertMessage from(AnswerInfo answerInfo);

    RankAnswerAlertMessage from(AlertDto.RankAnswerAlert rankAnswerAlert);

    CoffeeChatRequestAlertMessage from(CoffeeChatInfo coffeeChatInfo);
}
