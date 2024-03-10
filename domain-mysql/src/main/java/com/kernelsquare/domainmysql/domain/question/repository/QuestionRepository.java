package com.kernelsquare.domainmysql.domain.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kernelsquare.domainmysql.domain.question.entity.Question;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByClosedStatus(Boolean closedStatus);
}
