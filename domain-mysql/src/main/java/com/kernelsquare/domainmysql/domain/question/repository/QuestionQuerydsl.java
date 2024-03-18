package com.kernelsquare.domainmysql.domain.question.repository;

import com.kernelsquare.domainmysql.domain.question.dto.FindAllQuestions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionQuerydsl {
    Page<FindAllQuestions> findAllQuestions(Pageable pageable);
}
