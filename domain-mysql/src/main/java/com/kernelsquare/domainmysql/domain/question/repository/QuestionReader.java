package com.kernelsquare.domainmysql.domain.question.repository;

import com.kernelsquare.domainmysql.domain.question.entity.Question;

import java.util.List;

public interface QuestionReader {
    Question findQuestion(Long QuestionId);

    List<Question> findAllByClosedStatus(Boolean closedStatus);
}
