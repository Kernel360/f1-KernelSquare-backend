package com.kernelsquare.memberapi.common.oauth;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomInMemoryOAuth2AuthorizedClientService implements OAuth2AuthorizedClientService {

    private Map<String, OAuth2AuthorizedClient> authorizedClients = new HashMap<>();

    @Override
    public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
        String clientRegistrationId = authorizedClient.getClientRegistration().getRegistrationId();
        // 이 예제에서는 사용자 이름과 ClientRegistrationId를 키로 사용합니다.
        String key = principal.getName() + "_" + clientRegistrationId;
        authorizedClients.put(key, authorizedClient);
    }

    @Override
    public void removeAuthorizedClient(String clientRegistrationId, String principalName) {
        String key = principalName + "_" + clientRegistrationId;
        authorizedClients.remove(key);
    }

    @Override
    public OAuth2AuthorizedClient loadAuthorizedClient(String clientRegistrationId, String principalName) {
        String key = principalName + "_" + clientRegistrationId;
        return authorizedClients.get(key);
    }
}

