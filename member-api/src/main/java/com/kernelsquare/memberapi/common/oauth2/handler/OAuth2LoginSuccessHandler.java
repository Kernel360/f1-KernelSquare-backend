package com.kernelsquare.memberapi.common.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.kernelsquare.core.common_response.error.code.LevelErrorCode;
import com.kernelsquare.core.common_response.error.code.MemberErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.util.ExperiencePolicy;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.level.repository.LevelRepository;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;
import com.kernelsquare.domainmysql.domain.member_authority.repository.MemberAuthorityRepository;
import com.kernelsquare.memberapi.domain.auth.dto.LoginRequest;
import com.kernelsquare.memberapi.domain.auth.dto.LoginResponse;
import com.kernelsquare.memberapi.domain.auth.dto.MemberDetails;
import com.kernelsquare.memberapi.domain.auth.dto.TokenResponse;
import com.kernelsquare.memberapi.domain.auth.service.TokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final MemberRepository memberRepository;
    private final LevelRepository levelRepository;
    private final TokenProvider tokenProvider;
    private final MemberAuthorityRepository memberAuthorityRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        MemberDetails oAuth2User = (MemberDetails) authentication.getPrincipal();
        String email = oAuth2User.getEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

        List<MemberAuthority> memberAuthorities = memberAuthorityRepository.findAllByMember(member);


        LoginRequest loginRequest = LoginRequest.builder()
                .email(email)
                .password("password")
                .build();

        TokenResponse tokenResponse = tokenProvider.createToken(member, loginRequest);

        LoginResponse loginResponse = LoginResponse.of(member, tokenResponse, memberAuthorities);

        member.addExperience(ExperiencePolicy.MEMBER_DAILY_ATTENDED.getReward());
        if (member.isExperienceExceed(member.getExperience())) {
            member.updateExperience(member.getExperience() - member.getLevel().getLevelUpperLimit());
            Level nextLevel = levelRepository.findByName(member.getLevel().getName() + 1)
                    .orElseThrow(() -> new BusinessException(LevelErrorCode.LEVEL_NOT_FOUND));
            member.updateLevel(nextLevel);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        String json = new ObjectMapper().writeValueAsString(loginResponse);
        String encodedJson = Base64.getEncoder().encodeToString(json.getBytes());

        Cookie cookie = new Cookie("loginResponse", encodedJson);
        cookie.setMaxAge(600);
        cookie.setPath("/");
        cookie.setDomain(".kernelsquare.live");
        response.addCookie(cookie);

        // develop
        response.sendRedirect("http://dev.kernelsquare.live/oauth/github");
        // main
//        response.sendRedirect("https://kernelsquare.live/oauth/github");
    }
}