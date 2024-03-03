package com.kernelsquare.memberapi.common.oauth2.service;

import com.kernelsquare.core.type.SocialProvider;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;
import com.kernelsquare.domainmysql.domain.social_login.entity.SocialLogin;
import com.kernelsquare.domainmysql.domain.social_login.repository.SocialLoginRepository;
import com.kernelsquare.memberapi.common.oauth2.info.OAuth2UserInfo;
import com.kernelsquare.memberapi.common.oauth2.info.OAuth2UserInfoFactory;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdaptorInstance;
import com.kernelsquare.memberapi.domain.auth.dto.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2MemberService_bk implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final SocialLoginRepository socialLoginRepository;
    private final OAuth2UserService oAuth2UserService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            return process(userRequest);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest) {
        OAuth2User user = oAuth2UserService.loadUser(userRequest);
        // TODO 애초에 프론트에서 없는 소셜 타입으로 들어오지 않는데 체크를 해줘야 하나?
        SocialProvider providerType = SocialProvider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());

        Member savedUser = memberRepository.findAllByEmail(userInfo.getEmail());

        if (savedUser != null) {
            Optional<SocialLogin> checkSocial = socialLoginRepository.findById(savedUser.getId());
            if (checkSocial.isPresent()) {
                if (checkSocial.get().getSocialProvider() == providerType) {
                    // 로그인
                return MemberDetails.create(MemberAdaptorInstance.of(savedUser), user.getAttributes());
                }
            }
           // 추가 후 로그인
            createSocial(userInfo, providerType);
            return MemberDetails.create(MemberAdaptorInstance.of(savedUser), user.getAttributes());
        } else {
            String randomNickname = "테스트" + UUID.randomUUID().toString().replace("-", "");

            savedUser = createUser(userInfo, randomNickname);
            createSocial(userInfo, providerType);
        }
        return MemberDetails.create(MemberAdaptorInstance.of(savedUser), user.getAttributes());
    }

    private Member createUser(OAuth2UserInfo userInfo, String randomNickname) {
        Member member = Member.builder()
            .nickname(randomNickname)
            .email(userInfo.getEmail())
            .password("password")
            .experience(0L)
            .imageUrl("image_url")
            .introduction("introduction")
            .build();
        return memberRepository.saveAndFlush(member);
    }

    private void createSocial(OAuth2UserInfo userInfo, SocialProvider providerType) {
        SocialLogin socialLogin = SocialLogin.builder()
                .email(userInfo.getEmail())
                .socialProvider(providerType)
                .build();
        socialLoginRepository.saveAndFlush(socialLogin);
    }

}