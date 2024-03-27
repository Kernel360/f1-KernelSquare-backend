package com.kernelsquare.domainmysql.domain.answer.repository;

import com.kernelsquare.domainmysql.domain.answer.entity.Answer;
import com.kernelsquare.domainmysql.domain.rank.entity.Rank;

public interface AnswerStore {
    void upVote(Long answerId);

    void downVote(Long answerId);

    Answer store(Answer answer);

    void updateAnswerRank(Rank rank, Long answerId);

    void delete(Long answerId);
}
