package com.kernelsquare.memberapi.common.oauth2.service;

import com.kernelsquare.core.type.SocialProvider;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;
import com.kernelsquare.domainmysql.domain.social_login.entity.SocialLogin;
import com.kernelsquare.domainmysql.domain.social_login.repository.SocialLoginRepository;
import com.kernelsquare.memberapi.common.oauth2.CustomOAuth2User;
import com.kernelsquare.memberapi.common.oauth2.OAuthAttributes;
import com.kernelsquare.memberapi.common.oauth2.info.OAuth2UserInfo;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdaptorInstance;
import com.kernelsquare.memberapi.domain.auth.dto.MemberDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2MemberService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final SocialLoginRepository socialLoginRepository;

    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";
    private static final String GOOGLE = "google";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

        /**
         * OAuth2User는 OAuth 서비스에서 가져온 유저 정보를 담고 있는 유저
         */
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        /**
         * userRequest에서 registrationId 추출 후 registrationId으로 SocialType 저장
         * http://localhost:8501/oauth2/authorization/kakao에서 kakao가 registrationId
         * userNameAttributeName은 이후에 nameAttributeKey로 설정
         */
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialProvider socialType = getSocialType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); // OAuth2 로그인 시 키(PK)가 되는 값
        Map<String, Object> attributes = oAuth2User.getAttributes(); // 소셜 로그인에서 API가 제공하는 userInfo의 Json 값(유저 정보들)

        // socialType에 따라 유저 정보를 통해 OAuthAttributes 객체 생성
        OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes);

        Member createdMember = getMember(extractAttributes, socialType); // getMember() 메소드로 User 객체 생성 후 반환

        return MemberDetails.create(MemberAdaptorInstance.of(createdMember), attributes);
    }

    private SocialProvider getSocialType(String registrationId) {
        if(NAVER.equals(registrationId)) {
            return SocialProvider.NAVER;
        }
        if(KAKAO.equals(registrationId)) {
            return SocialProvider.KAKAO;
        }
        if(GOOGLE.equals(registrationId)) {
            return SocialProvider.GOOGLE;
        }
        return SocialProvider.GITHUB;
    }

    private Member getMember(OAuthAttributes attributes, SocialProvider socialType) {
        Member findMember = memberRepository.findAllByEmail(attributes.getOauth2UserInfo().getId());

        if (findMember != null) {
            Optional<SocialLogin> checkSocial = socialLoginRepository.findById(findMember.getId());
            if (checkSocial.isPresent()) {
                if (checkSocial.get().getSocialProvider() == socialType) {
                    // 로그인
                    return findMember;
                }
            }
            // 추가 후 로그인
            createSocial(attributes, socialType);
            return findMember;
        }
        return saveMemberWithSocial(attributes, socialType);
    }

//    private Member saveMember(OAuthAttributes attributes) {
//        Member createdMember = attributes.toEntity(attributes.getOauth2UserInfo());
//        return memberRepository.save(createdMember);
//    }

    private Member saveMemberWithSocial(OAuthAttributes attributes, SocialProvider socialType) {
        Member createdMember = attributes.toEntity(attributes.getOauth2UserInfo());
        createSocial(attributes, socialType);
        return memberRepository.save(createdMember);
    }

    private void createSocial(OAuthAttributes attributes, SocialProvider providerType) {
        SocialLogin socialLogin = SocialLogin.builder()
                .email(attributes.getOauth2UserInfo().getEmail())
                .socialProvider(providerType)
                .build();
        socialLoginRepository.save(socialLogin);
    }
}