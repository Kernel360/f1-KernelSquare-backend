package com.kernelsquare.memberapi.common.oauth2;

import com.kernelsquare.core.type.SocialProvider;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.memberapi.common.oauth2.info.OAuth2UserInfo;
import com.kernelsquare.memberapi.common.oauth2.info.impl.GithubOAuth2UserInfo;
import com.kernelsquare.memberapi.common.oauth2.info.impl.GoogleOAuth2UserInfo;
import com.kernelsquare.memberapi.common.oauth2.info.impl.KakaoOAuth2UserInfo;
import com.kernelsquare.memberapi.common.oauth2.info.impl.NaverOAuth2UserInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 각 소셜에서 받아오는 데이터가 다르므로
 * 소셜별로 데이터를 받는 데이터를 분기 처리하는 DTO 클래스
 */
@Getter
public class OAuthAttributes {

    private String nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드 값, PK와 같은 의미
    private OAuth2UserInfo oauth2UserInfo; // 소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등등)

    @Builder
    public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oauth2UserInfo;
    }

    /**
     * SocialType에 맞는 메소드 호출하여 OAuthAttributes 객체 반환
     * 파라미터 : userNameAttributeName -> OAuth2 로그인 시 키(PK)가 되는 값 / attributes : OAuth 서비스의 유저 정보들
     * 소셜별 of 메소드(ofGoogle, ofKaKao, ofNaver)들은 각각 소셜 로그인 API에서 제공하는
     * 회원의 식별값(id), attributes, nameAttributeKey를 저장 후 build
     */
    public static OAuthAttributes of(SocialProvider socialType,
                                     String userNameAttributeName, Map<String, Object> attributes) {

        if (socialType == SocialProvider.NAVER) {
            return ofNaver(userNameAttributeName, attributes);
        }
        if (socialType == SocialProvider.KAKAO) {
            return ofKakao(userNameAttributeName, attributes);
        }
        if (socialType == SocialProvider.GOOGLE) {
            return ofGoogle(userNameAttributeName, attributes);
        }
        return ofGithub(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuthAttributes ofGithub(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new GithubOAuth2UserInfo(attributes))
                .build();
    }

    /**
     * of메소드로 OAuthAttributes 객체가 생성되어, 유저 정보들이 담긴 OAuth2UserInfo가 소셜 타입별로 주입된 상태
     */
    public Member toEntity(OAuth2UserInfo oauth2UserInfo, Level level) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return Member.builder()
//                .nickname("테스트" + UUID.randomUUID().toString().replace("-", ""))
                .nickname(oauth2UserInfo.getEmail())
                .email(oauth2UserInfo.getEmail())
                .level(level)
                .password(passwordEncoder.encode("password"))
                .experience(0L)
                .imageUrl("image_url")
                .introduction("introduction")
                .build();
    }
}