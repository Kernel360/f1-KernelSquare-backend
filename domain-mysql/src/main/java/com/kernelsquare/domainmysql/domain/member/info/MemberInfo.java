package com.kernelsquare.domainmysql.domain.member.info;

import com.kernelsquare.core.util.ImageUtils;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberInfo {
    private final Long id;
    private final String nickname;
    private final Long experience;
    private final String imageUrl;
    private final String introduction;
    private final Long level;

    @Builder
    public MemberInfo(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.experience = member.getExperience();
        this.imageUrl = ImageUtils.makeImageUrl(member.getImageUrl());
        this.introduction = member.getIntroduction();
        this.level = member.getLevel().getName();
    }

    public static MemberInfo from(Member member) {
        return MemberInfo.builder()
            .member(member)
            .build();
    }
}
