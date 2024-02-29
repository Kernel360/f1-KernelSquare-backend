package com.kernelsquare.domainmysql.domain.question.repository;

import com.kernelsquare.core.common_response.error.code.QuestionErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QuestionReaderImpl implements QuestionReader {
    private final QuestionRepository questionRepository;

    @Override
    public Question findQuestion(Long questionId) {
        return questionRepository.findById(questionId)
            .orElseThrow(() -> new BusinessException(QuestionErrorCode.QUESTION_NOT_FOUND));
    }

    @Override
    public List<Question> findClosedQuestions(Boolean closedStatus) {
        return questionRepository.findAllByClosedStatus(closedStatus);
    }
}
