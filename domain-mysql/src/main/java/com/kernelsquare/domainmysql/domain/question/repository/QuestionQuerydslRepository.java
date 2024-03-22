package com.kernelsquare.domainmysql.domain.question.repository;

import com.kernelsquare.domainmysql.domain.question.info.QuestionInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionQuerydslRepository {
    Page<QuestionInfo.FindAllQuestionsInfo> findAllQuestions(Pageable pageable);
}
