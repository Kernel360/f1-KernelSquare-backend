package com.kernelsquare.memberapi.common.oauth2.handler;

import com.kernelsquare.memberapi.common.oauth2.CustomOAuth2User;
import com.kernelsquare.memberapi.domain.auth.service.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
//@Transactional
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
//
//    private final TokenProvider tokenProvider;
//
//    public OAuth2LoginSuccessHandler(TokenProvider tokenProvider) {
//        this.tokenProvider = tokenProvider;
//    }
//
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("OAuth2 Login 성공!");
        try {
//            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            // TODO 리퀘스트를 만들어서 넣어주는 방법도 있고 오버로딩으로 받아서 구현하는 방법도 있다.
            // TODO 리스폰스로 같이 반환하면서 리다이렉트 보내줘도 될듯

//            // 사용자의 이메일과 역할을 가져와서 액세스 토큰 생성
//            String email = (String) oAuth2User.getAttributes().get("email");
//            String roles = extractRoles(oAuth2User);
//            String accessToken = tokenProvider.createAccessToken(email, roles);
//            String refreshToken = tokenProvider.createRefreshToken();
//
//            // 토큰을 응답으로 전송
//            response.addHeader("Authorization", "Bearer " + accessToken);
//            response.addHeader("Refresh-Token", refreshToken);

            // 리다이렉트
            response.sendRedirect("/api/v1/questions");
        } catch (Exception e) {
            throw e;
        }
    }
//
//    private String extractRoles(CustomOAuth2User oAuth2User) {
//        return oAuth2User.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));
//    }
}