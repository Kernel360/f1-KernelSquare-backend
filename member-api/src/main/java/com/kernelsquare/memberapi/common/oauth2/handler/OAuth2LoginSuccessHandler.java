package com.kernelsquare.memberapi.common.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.memberapi.common.oauth2.CustomOAuth2User;
import com.kernelsquare.memberapi.domain.auth.dto.LoginRequest;
import com.kernelsquare.memberapi.domain.auth.dto.LoginResponse;
import com.kernelsquare.memberapi.domain.auth.dto.MemberDetails;
import com.kernelsquare.memberapi.domain.auth.dto.TokenResponse;
import com.kernelsquare.memberapi.domain.auth.service.AuthService;
import com.kernelsquare.memberapi.domain.auth.service.TokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.util.Base64;
import java.util.stream.Collectors;

import static com.kernelsquare.core.common_response.response.code.AuthResponseCode.LOGIN_SUCCESS;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("OAuth2 Login 성공!");

        MemberDetails oAuth2User = (MemberDetails) authentication.getPrincipal();

//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        String email = userDetails.getUsername();
        String email = oAuth2User.getEmail();
        log.info("사용자 이메일: {}", email);

        String url = "http://localhost:8501/api/v1/auth/login";

        // 로그인 요청 생성
        LoginRequest loginRequest = new LoginRequest(email, "password");

        // ObjectMapper를 생성하고 snake case 설정
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        // RestTemplate 생성 및 ObjectMapper 설정
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().forEach(converter -> {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                ((MappingJackson2HttpMessageConverter) converter).setObjectMapper(objectMapper);
            }
        });

        // POST 요청 보내고 응답 받기
        ResponseEntity<ApiResponse<LoginResponse>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(loginRequest),
                new ParameterizedTypeReference<>() {
                }
        );
        log.info("1번 추출중: {}", responseEntity);

        // 응답 객체 추출
        ApiResponse<LoginResponse> apiResponse = responseEntity.getBody();
        log.info("2번 추출중: {}", apiResponse);

        // TODO 쿠키 인코딩 방법
        // JSON 데이터를 Base64로 인코딩
        String json = new ObjectMapper().writeValueAsString(apiResponse);
        String encodedJson = Base64.getEncoder().encodeToString(json.getBytes());
        log.info("json : {}", json);

    // 쿠키에 Base64로 인코딩된 JSON 데이터 추가
        Cookie cookie = new Cookie("loginResponse", encodedJson);
        cookie.setMaxAge(3600); // 쿠키 유지 시간 (초 단위)
        cookie.setPath("/"); // 쿠키의 유효 범위 설정
        response.addCookie(cookie);
        log.info("Cookie : {}", encodedJson);

        // 로그인 후 리다이렉트
        response.sendRedirect("/api/v1/test");


        // TODO 세션 방법
//        response.setStatus(responseEntity.getStatusCodeValue());
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));

//        // 세션에 JSON 데이터 추가
//        HttpSession session = request.getSession(); // 세션 가져오기
//        session.setAttribute("loginResponse", apiResponse); // 세션에 데이터 저장
//        log.info("Session : {}", session.getAttribute("loginResponse"));
//
//        // 세션 유지 시간 설정 (예: 1시간)
//        session.setMaxInactiveInterval(3600);
    }
}