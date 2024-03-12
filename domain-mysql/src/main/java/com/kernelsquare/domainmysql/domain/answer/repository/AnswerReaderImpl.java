package com.kernelsquare.domainmysql.domain.answer.repository;

import com.kernelsquare.core.common_response.error.code.AnswerErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.constants.SystemConstants;
import com.kernelsquare.domainmysql.domain.answer.entity.Answer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AnswerReaderImpl implements AnswerReader {
    private final AnswerRepository answerRepository;

    @Override
    public Answer findAnswer(Long answerId) {
        return answerRepository.findById(answerId)
            .orElseThrow(() -> new BusinessException(AnswerErrorCode.ANSWER_NOT_FOUND));
    }

    @Override
    public List<Answer> findAnswers(Long questionId) {
        return answerRepository.findAnswersByQuestionIdSortedByCreationDate(questionId);
    }

    @Override
    public List<Answer> findAnswersTop3(Long questionId) {
        return answerRepository.findAnswersByQuestionIdSortedByVoteCount(questionId, SystemConstants.ANSWER_BOT);
    }
}
