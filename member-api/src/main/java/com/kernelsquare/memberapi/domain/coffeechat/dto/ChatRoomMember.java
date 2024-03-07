package com.kernelsquare.memberapi.domain.coffeechat.dto;


import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.core.util.ImageUtils;
import lombok.Builder;

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
}