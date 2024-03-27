package com.kernelsquare.memberapi.domain.chatgpt.facade;

import com.kernelsquare.domainmysql.domain.answer.info.AnswerInfo;
import com.kernelsquare.memberapi.domain.alert.mapper.AlertDtoMapper;
import com.kernelsquare.memberapi.domain.alert.service.AlertService;
import com.kernelsquare.memberapi.domain.chatgpt.service.ChatGptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatGptFacade {
    private final ChatGptService chatGptService;
    private final AlertService alertService;
    private final AlertDtoMapper alertDtoMapper;

    public void createChatGptAnswer(Long questionId) {
        AnswerInfo answerInfo = chatGptService.createChatGptAnswer(questionId);
        alertService.sendToBroker(alertDtoMapper.from(answerInfo));
    }
}
