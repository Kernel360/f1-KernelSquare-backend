package com.kernelsquare.domainmysql.domain.answer.repository;

import com.kernelsquare.domainmysql.domain.answer.entity.Answer;

public interface AnswerStore {
    void upVote(Long answerId);

    void downVote(Long answerId);

    void store(Answer answer);
}