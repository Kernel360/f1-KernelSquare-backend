package com.kernelsquare.memberapi.common.oauth2.info;

import com.kernelsquare.core.type.SocialProvider;
import com.kernelsquare.memberapi.common.oauth2.info.impl.GithubOAuth2UserInfo;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(SocialProvider providerType, Map<String, Object> attributes) {
        switch (providerType) {
//            case GOOGLE: return new GoogleOAuth2UserInfo(attributes);
//            case FACEBOOK: return new FacebookOAuth2UserInfo(attributes);
//            case NAVER: return new NaverOAuth2UserInfo(attributes);
//            case KAKAO: return new KakaoOAuth2UserInfo(attributes);
            case GITHUB: return new GithubOAuth2UserInfo(attributes);
            // TODO 따로 커스텀 에러 처리
            default: throw new IllegalArgumentException("Invalid Provider Type.");
        }
    }
}

