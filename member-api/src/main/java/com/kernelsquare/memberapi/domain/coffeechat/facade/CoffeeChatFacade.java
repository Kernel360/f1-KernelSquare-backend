package com.kernelsquare.memberapi.domain.coffeechat.facade;

import com.kernelsquare.domainmysql.domain.coffeechat.info.CoffeeChatInfo;
import com.kernelsquare.memberapi.domain.alert.mapper.AlertDtoMapper;
import com.kernelsquare.memberapi.domain.alert.service.AlertService;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.memberapi.domain.coffeechat.mapper.CoffeeChatDtoMapper;
import com.kernelsquare.memberapi.domain.coffeechat.service.CoffeeChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoffeeChatFacade {
    private final CoffeeChatDtoMapper coffeeChatDtoMapper;
    private final CoffeeChatService coffeeChatService;
    private final AlertDtoMapper alertDtoMapper;
    private final AlertService alertService;

    public void sendCoffeeChatRequest(MemberAdapter memberAdapter, Long memberId) {
        CoffeeChatInfo coffeeChatInfo = coffeeChatService.coffeeChatRequest(coffeeChatDtoMapper.toCommand(memberAdapter, memberId));
        alertService.storeAndSendAlert(alertDtoMapper.from(coffeeChatInfo));
    }
}
