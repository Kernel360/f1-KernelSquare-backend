package com.kernelsquare.domainmysql.domain.question.repository;

import com.kernelsquare.core.common_response.error.code.QuestionErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.question.dto.FindAllQuestions;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QuestionReaderImpl implements QuestionReader {
    private final QuestionRepository questionRepository;
    private final QuestionQuerydslRepository questionQuerydslRepository;

    @Override
    public Question findQuestion(Long questionId) {
        return questionRepository.findById(questionId)
            .orElseThrow(() -> new BusinessException(QuestionErrorCode.QUESTION_NOT_FOUND));
    }

    @Override
    public Page<FindAllQuestions> findAllQuestions(Pageable pageable) {
        return questionQuerydslRepository.findAllQuestions(pageable);
    }

    @Override
    public List<Question> findClosedQuestions(Boolean closedStatus) {
        return questionRepository.findAllByClosedStatus(closedStatus);
    }
}
