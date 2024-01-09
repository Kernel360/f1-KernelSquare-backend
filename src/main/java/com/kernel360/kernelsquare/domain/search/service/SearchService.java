package com.kernel360.kernelsquare.domain.search.service;

import com.kernel360.kernelsquare.domain.question.dto.FindQuestionResponse;
import com.kernel360.kernelsquare.domain.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final QuestionRepository questionRepository;

    public List<FindQuestionResponse> searchQuestions(String keyword) {
        return questionRepository.searchByKeyword(keyword).stream().map(question -> FindQuestionResponse.of(question.getMember(), question, question.getMember().getLevel())).toList();
    }
}
