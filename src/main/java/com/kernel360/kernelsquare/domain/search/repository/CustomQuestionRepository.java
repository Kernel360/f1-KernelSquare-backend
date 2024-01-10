package com.kernel360.kernelsquare.domain.search.repository;

import com.kernel360.kernelsquare.domain.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomQuestionRepository {
    Page<Question> searchByKeyword(Pageable pageable, String keyword);
}
