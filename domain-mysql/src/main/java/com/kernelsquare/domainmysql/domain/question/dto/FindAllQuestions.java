package com.kernelsquare.domainmysql.domain.question.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FindAllQuestions {
    private final Long id;
    private final String title;
    private final String imageUrl;
    private final Long viewCount;
    private final Boolean closedStatus;
    private final Long memberId;
    private final String nickname;
    private final String memberImageUrl;
    private final Long level;
    private final String levelImageUrl;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;
    private final String skills;

    @QueryProjection
    public FindAllQuestions(Long id, String title, String imageUrl, Long viewCount, Boolean closedStatus,
                            Long memberId, String nickname, String memberImageUrl, Long level, String levelImageUrl,
                            LocalDateTime createdDate, LocalDateTime modifiedDate, String skills) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.viewCount = viewCount;
        this.closedStatus = closedStatus;
        this.memberId = memberId;
        this.nickname = nickname;
        this.memberImageUrl = memberImageUrl;
        this.level = level;
        this.levelImageUrl = levelImageUrl;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.skills = skills;
    }
}
