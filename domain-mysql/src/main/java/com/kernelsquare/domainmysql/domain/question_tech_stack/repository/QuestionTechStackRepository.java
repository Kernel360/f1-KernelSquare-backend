package com.kernelsquare.domainmysql.domain.question_tech_stack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kernelsquare.domainmysql.domain.question_tech_stack.entity.QuestionTechStack;

public interface QuestionTechStackRepository extends JpaRepository<QuestionTechStack, Long> {
	void deleteAllByQuestionId(Long questionId);
}
