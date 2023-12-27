package com.kernel360.kernelsquare.domain.answer.repository;

import com.kernel360.kernelsquare.domain.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query("SELECT a FROM answer a WHERE a.question.id = :questionId ORDER BY a.createdDate DESC")
    List<Answer> findAnswersByQuestionIdSortedByCreationDate(@Param("questionId") Long questionId);
}
