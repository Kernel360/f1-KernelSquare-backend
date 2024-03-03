package com.kernelsquare.memberapi.common.oauth2.info.impl;

import com.kernelsquare.memberapi.common.oauth2.info.OAuth2UserInfo;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getName() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        if (account == null || profile == null) {
            return null;
        }

        return (String) profile.get("name");
    }

    @Override
    public String getEmail() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        if (account == null || profile == null) {
            return null;
        }

        return (String) profile.get("email");
    }

    @Override
    public String getImageUrl() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        if (account == null || profile == null) {
            return null;
        }

        return (String) profile.get("thumbnail_image_url");
    }
}
