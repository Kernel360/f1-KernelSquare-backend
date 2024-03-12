package com.kernelsquare.domainmysql.domain.answer.repository;

import com.kernelsquare.domainmysql.domain.answer.entity.Answer;

import java.util.List;

public interface AnswerReader {
    Answer findAnswer(Long answerId);

    List<Answer> findAnswers(Long questionId);

    List<Answer> findAnswersTop3(Long questionId);
}
