package com.kernelsquare.domainmysql.domain.coffeechat.info;

import com.kernelsquare.domainmysql.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CoffeeChatInfo {
    private final String recipientId;
    private final String recipient;
    private final String senderId;
    private final String sender;

    public static CoffeeChatInfo of(Member sender, Member recipient) {
        return CoffeeChatInfo.builder()
            .recipientId(recipient.getId().toString())
            .recipient(recipient.getNickname())
            .senderId(sender.getId().toString())
            .sender(sender.getNickname())
            .build();
    }
}
