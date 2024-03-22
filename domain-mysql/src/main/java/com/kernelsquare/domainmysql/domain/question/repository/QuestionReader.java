package com.kernelsquare.domainmysql.domain.question.repository;

import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.domainmysql.domain.question.info.QuestionInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionReader {
    Question findQuestion(Long QuestionId);

    Page<QuestionInfo.FindAllQuestionsInfo> findAllQuestions(Pageable pageable);

    List<Question> findClosedQuestions(Boolean closedStatus);
}
