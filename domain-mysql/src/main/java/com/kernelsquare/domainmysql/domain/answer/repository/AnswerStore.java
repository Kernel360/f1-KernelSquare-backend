package com.kernelsquare.domainmysql.domain.answer.repository;

import com.kernelsquare.domainmysql.domain.answer.entity.Answer;

public interface AnswerStore {
	Answer store(Answer answer);
}
