package com.kernel360.kernelsquare.domain.question_tech_stack.repository;

import com.kernel360.kernelsquare.domain.question_tech_stack.entity.QuestionTechStack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionTechStackRepository extends JpaRepository<QuestionTechStack, Long> {
    void deleteAllByQuestionId(Long questionId);
}
