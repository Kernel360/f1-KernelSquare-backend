package com.kernelsquare.domainmysql.domain.auth.info;

import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.info.MemberInfo;
import lombok.Builder;

import java.util.List;

public class AuthInfo {
    @Builder
    public record LoginInfo(
        Long memberId,
        String nickname,
        Long experience,
        String introduction,
        String imageUrl,
        Long level,
        List<String> roles,
        String accessToken,
        String refreshToken
    ) {
        public static LoginInfo of(MemberInfo memberInfo, List<String> roles, String accessToken, String refreshToken) {
            return LoginInfo.builder()
                .memberId(memberInfo.getId())
                .nickname(memberInfo.getNickname())
                .experience(memberInfo.getExperience())
                .introduction(memberInfo.getIntroduction())
                .imageUrl(memberInfo.getImageUrl())
                .level(memberInfo.getLevel())
                .roles(roles)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        }
    }
}
