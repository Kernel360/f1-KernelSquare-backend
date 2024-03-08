package com.kernelsquare.domainmysql.domain.coffeechat.command;

import com.kernelsquare.domainmysql.domain.member.entity.Member;
import lombok.Builder;

public class CoffeeChatCommand {
    @Builder
    public record RequestCommand(
        Member sender,
        Long recipientId
    ) {}
}
