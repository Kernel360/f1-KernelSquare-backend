package com.kernelsquare.memberapi.common.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.kernelsquare.core.common_response.error.code.LevelErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.util.ExperiencePolicy;
import com.kernelsquare.domainmysql.domain.auth.info.AuthInfo;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.level.repository.LevelRepository;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.info.MemberInfo;
import com.kernelsquare.domainmysql.domain.member.repository.MemberReader;
import com.kernelsquare.memberapi.domain.auth.dto.AuthDto;
import com.kernelsquare.memberapi.domain.auth.dto.MemberDetails;
import com.kernelsquare.memberapi.domain.auth.mapper.AuthDtoMapper;
import com.kernelsquare.memberapi.domain.auth.service.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final LevelRepository levelRepository;
    private final TokenProvider tokenProvider;
    private final MemberReader memberReader;
    private final AuthDtoMapper authDtoMapper;

    @Value("${custom.github.redirect}")
    private String githubRedirectUrl;
    @Value("${custom.cookie.domain}")
    private String cookieDomain;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        MemberDetails oAuth2User = (MemberDetails) authentication.getPrincipal();
        String email = oAuth2User.getEmail();
        Member member = memberReader.findMember(email);

        AuthInfo.LoginInfo loginInfo = tokenProvider.createToken(MemberInfo.from(member));

        AuthDto.LoginResponse loginResponse = authDtoMapper.of(loginInfo);

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

        String cookieValue = ResponseCookie.from("loginResponse", encodedJson) // 쿠키 이름과 값 설정
                .domain(cookieDomain) // 쿠키 도메인 설정 / 로컬에서 돌리실 땐 .localhost로 변경하셔야 합니다.
                .maxAge(600) // 최대 유효 시간 설정 (초 단위)
                .path("/") // 쿠키 경로 설정
                .build()
                .toString(); // 최종 쿠키 문자열로 변환

        response.setHeader("Set-Cookie", cookieValue); // HTTP 응답 헤더에 쿠키 추가

        response.sendRedirect(githubRedirectUrl);
    }
}