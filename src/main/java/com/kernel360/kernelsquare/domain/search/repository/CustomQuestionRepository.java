package com.kernel360.kernelsquare.domain.search.repository;

import com.kernel360.kernelsquare.domain.question.entity.Question;

import java.util.List;

public interface CustomQuestionRepository {
    List<Question> searchByKeyword(String keyword);
}
