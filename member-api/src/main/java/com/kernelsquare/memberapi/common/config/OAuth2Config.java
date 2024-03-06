package com.kernelsquare.memberapi.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;

@Configuration
@RequiredArgsConstructor
public class OAuth2Config {
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public OAuth2UserService oAuth2UserService() {
        return new DefaultOAuth2UserService();
    }

//    @Bean
//    public OAuth2AuthorizedClientService oAuth2AuthorizedClientService() {
//        return new JdbcOAuth2AuthorizedClientService();
//    }
}