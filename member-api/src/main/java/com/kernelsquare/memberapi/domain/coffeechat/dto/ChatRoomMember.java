package com.kernelsquare.memberapi.domain.coffeechat.dto;


import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.core.util.ImageUtils;
import lombok.Builder;

import java.util.Objects;

@Builder
public record ChatRoomMember(
    Long memberId,
    String nickname,
    String memberImageUrl
) {
    public static ChatRoomMember from(Member member) {
        return ChatRoomMember.builder()
            .memberId(member.getId())
            .nickname(member.getNickname())
            .memberImageUrl(ImageUtils.makeImageUrl(member.getImageUrl()))
            .build();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ChatRoomMember that = (ChatRoomMember) obj;
        return memberId.equals(that.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId);
    }
}