package com.kernelsquare.memberapi.domain.chatgpt.service;

import com.kernelsquare.domainmysql.domain.answer.info.AnswerInfo;

public interface ChatGptService {
	AnswerInfo createChatGptAnswer(Long questionId);
}
