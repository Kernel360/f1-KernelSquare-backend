package com.kernelsquare.memberapi.common.oauth2.service;

import com.kernelsquare.core.common_response.error.code.AuthorityErrorCode;
import com.kernelsquare.core.common_response.error.code.LevelErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.type.AuthorityType;
import com.kernelsquare.core.type.SocialProvider;
import com.kernelsquare.domainmysql.domain.authority.entity.Authority;
import com.kernelsquare.domainmysql.domain.authority.repository.AuthorityRepository;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.level.repository.LevelRepository;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;
import com.kernelsquare.domainmysql.domain.member_authority.repository.MemberAuthorityRepository;
import com.kernelsquare.domainmysql.domain.social_login.entity.SocialLogin;
import com.kernelsquare.domainmysql.domain.social_login.repository.SocialLoginRepository;
import com.kernelsquare.memberapi.common.oauth2.CustomOAuth2User;
import com.kernelsquare.memberapi.common.oauth2.OAuthAttributes;
import com.kernelsquare.memberapi.common.oauth2.info.OAuth2UserInfo;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdaptorInstance;
import com.kernelsquare.memberapi.domain.auth.dto.MemberDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2MemberService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final SocialLoginRepository socialLoginRepository;
    private final LevelRepository levelRepository;
    private final AuthorityRepository authorityRepository;
    private final MemberAuthorityRepository memberAuthorityRepository;

    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";
    private static final String GOOGLE = "google";

    @Override
    @Transactional
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

        if (extractAttributes.getOauth2UserInfo().getEmail() == null) {
            throw new RuntimeException("사용자 정보에 이메일이 없습니다.");
        }

        Member createdMember = getMember(extractAttributes, socialType); // getMember() 메소드로 Member 객체 생성 후 반환

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
        Boolean checkExistMember = memberRepository.existsByEmail(attributes.getOauth2UserInfo().getEmail());
        Boolean checkExistSocialLogin = socialLoginRepository.existsByEmailAndSocialProvider(
                attributes.getOauth2UserInfo().getEmail(), socialType);

        if (!checkExistMember) {
            saveMemberWithSocial(attributes, socialType);
            Member findMember = memberRepository.findByEmail(attributes.getOauth2UserInfo().getEmail())
                    .orElseThrow(() -> new RuntimeException("소셜 회원가입 중 오류가 발생했습니다1."));
            return findMember;
        }

        if(!checkExistSocialLogin) {
            // TODO social 만 추가 후 로그인
            createSocial(attributes, socialType);
            Member findMember = memberRepository.findByEmail(attributes.getOauth2UserInfo().getEmail())
                    .orElseThrow(() -> new RuntimeException("소셜 회원가입 중 오류가 발생했습니다2."));
            return findMember;
        }

        Member findMember = memberRepository.findByEmail(attributes.getOauth2UserInfo().getEmail())
                .orElseThrow(() -> new RuntimeException("소셜 회원가입 중 오류가 발생했습니다3."));

        return findMember;
    }

    private Member saveMemberWithSocial(OAuthAttributes attributes, SocialProvider socialType) {
        Level level = levelRepository.findByName(1L)
                .orElseThrow(() -> new BusinessException(LevelErrorCode.LEVEL_NOT_FOUND));

        Authority authority = authorityRepository.findAuthorityByAuthorityType(AuthorityType.ROLE_USER)
                .orElseThrow(() -> new BusinessException(AuthorityErrorCode.AUTHORITY_NOT_FOUND));

        Member createdMember = attributes.toEntity(attributes.getOauth2UserInfo(), level);
        MemberAuthority memberAuthority = MemberAuthority.builder().member(createdMember).authority(authority).build();
        memberAuthorityRepository.save(memberAuthority);

        Member savedMember = memberRepository.save(createdMember);

        List<MemberAuthority> authorities = List.of(memberAuthority);
        savedMember.initAuthorities(authorities);

        createSocial(attributes, socialType);
        return savedMember;
    }

    private void createSocial(OAuthAttributes attributes, SocialProvider providerType) {
        SocialLogin socialLogin = SocialLogin.builder()
                .email(attributes.getOauth2UserInfo().getEmail())
                .socialProvider(providerType)
                .build();
        socialLoginRepository.save(socialLogin);
    }
}