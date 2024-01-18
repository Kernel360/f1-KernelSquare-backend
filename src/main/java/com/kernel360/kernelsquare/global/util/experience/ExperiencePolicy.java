package com.kernel360.kernelsquare.global.util.experience;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ExperiencePolicy implements ExperienceLogic{
    QUESTION_CREATED(20L),
    ANSWER_CREATED(30L),
    ANSWER_ACCEPTED(10L),
    ANSWER_RANKED_GOLD(300L),
    ANSWER_RANKED_SILVER(200L),
    ANSWER_RANKED_BRONZE(100L),
    MEMBER_DAILY_ATTENDED(10L),
    MEMBER_WEEKLY_ATTENDED(50L),
    MEMBER_MONTHLY_ATTENDED(150L);

    private final Long reward;

    @Override
    public Long getReward() {return reward;}
}
