package com.kernelsquare.memberapi.domain.answer.facade;

import com.kernelsquare.domainmysql.domain.stream.service.SseService;
import com.kernelsquare.memberapi.domain.answer.dto.AnswerDto;
import com.kernelsquare.memberapi.domain.answer.mapper.AnswerDtoMapper;
import com.kernelsquare.memberapi.domain.answer.service.AnswerService;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerFacade {
    private final AnswerService answerService;
    private final AnswerDtoMapper answerDtoMapper;
    private final SseService sseService;

    public void createAnswer(AnswerDto.CreateRequest request, Long questionId, MemberAdapter memberAdapter) {
        answerService.createAnswer(answerDtoMapper.toCommand(request, questionId, memberAdapter));
//        sseService.notify(memberAdapter.getMember().getId(), "?", "notify");
    }
}