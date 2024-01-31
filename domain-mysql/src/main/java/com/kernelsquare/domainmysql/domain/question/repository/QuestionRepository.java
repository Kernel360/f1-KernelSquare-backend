package com.kernelsquare.domainmysql.domain.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kernelsquare.domainmysql.domain.question.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
