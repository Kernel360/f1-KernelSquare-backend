package com.kernelsquare.domainmysql.domain.answer.repository;

import com.kernelsquare.domainmysql.domain.answer.entity.Answer;

import java.util.List;

public interface AnswerReader {
    List<Answer> findAnswersByQuestionIdSortedByCreationDate(Long questionId);

    List<Answer> findAnswersByQuestionIdSortedByVoteCount(Long questionId);
}
