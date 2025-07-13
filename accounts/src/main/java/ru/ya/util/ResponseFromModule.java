package ru.ya.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.ya.model.Operation;

@Component
public class ResponseFromModule {
    @Value("${spring.application.name}")
    private String moduleName;

    @Value("${module-notifications}")
    private String moduleNotificationsHost;

    @Autowired
    OAuth2AuthorizedClientManager manager;

    @Autowired
    private RestClient.Builder restClientBuilder;

    public String getResponseFromModuleNotifications(String url, Operation operation) {
        OAuth2AuthorizedClient client = manager.authorize(OAuth2AuthorizeRequest
                .withClientRegistrationId(moduleName)
                .principal("system") // У client_credentials нет имени пользователя, поэтому будем использовать system.
                .build()
        );

        String accessToken = client.getAccessToken().getTokenValue();

        String ans = restClientBuilder.build().post()
                .uri(moduleNotificationsHost + url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // Подставляем токен доступа в заголовок Authorization
                .body(operation)
                .retrieve()
                .toEntity(String.class)
                .getBody();

        return ans;
    }

    private String getFallback(Throwable throwable) {
        System.out.println("Fallback executed. Error: " + throwable.getMessage());
        return new String("service-is-unavailable");
    }
}
