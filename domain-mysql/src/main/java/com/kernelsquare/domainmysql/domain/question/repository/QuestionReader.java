package com.kernelsquare.domainmysql.domain.question.repository;

import com.kernelsquare.domainmysql.domain.question.dto.FindAllQuestions;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionReader {
    Question findQuestion(Long QuestionId);

    Page<FindAllQuestions> findAllQuestions(Pageable pageable);

    List<Question> findClosedQuestions(Boolean closedStatus);
}
