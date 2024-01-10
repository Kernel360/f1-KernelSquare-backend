package com.kernel360.kernelsquare.domain.question.repository;

import com.kernel360.kernelsquare.domain.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
