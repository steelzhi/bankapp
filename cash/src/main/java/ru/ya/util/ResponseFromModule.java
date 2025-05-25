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
import ru.ya.enums.SuccessfullOperation;
import ru.ya.model.Cash;
import ru.ya.model.Operation;

@Component
public class ResponseFromModule {
    @Value("${spring.application.name}")
    private String moduleName;

    @Value("${module-accounts}")
    private String moduleAccountsHost;

    @Value("${module-notifications}")
    private String moduleNotificationsHost;

    @Autowired
    OAuth2AuthorizedClientManager manager;

    public String getResponseFromModuleAccounts(String url, Cash cash) {
        return getResponseFromModule(moduleAccountsHost, url, cash);
    }

    public String getResponseForSuccessfullOpFromModuleNotifications(String url, Operation operation) {
        return getResponseFromModule(moduleNotificationsHost, url, operation);
    }

    private String getResponseFromModule(String moduleNameForRequest, String url, Object object) {
        RestClient restClient = RestClient.create(moduleNameForRequest);
        OAuth2AuthorizedClient client = manager.authorize(OAuth2AuthorizeRequest
                .withClientRegistrationId(moduleName)
                .principal("system") // У client_credentials нет имени пользователя, поэтому будем использовать system.
                .build()
        );

        String accessToken = client.getAccessToken().getTokenValue();
        ResponseEntity<String> responseEntity = null;
        if (object instanceof Cash cash) {
            responseEntity = restClient.post()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // Подставляем токен доступа в заголовок Authorization
                    .body(cash)
                    .retrieve()
                    .toEntity(String.class);
        } else if (object instanceof Operation operation) {
            responseEntity = restClient.post()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // Подставляем токен доступа в заголовок Authorization
                    .body(operation)
                    .retrieve()
                    .toEntity(String.class);
        }

        return responseEntity.getBody();
    }


/*    public String getResponseFromModuleNotifications(String url, Operation operation) {
        RestClient restClient = RestClient.create(moduleNotificationsHost);
        OAuth2AuthorizedClient client = manager.authorize(OAuth2AuthorizeRequest
                .withClientRegistrationId(moduleName)
                .principal("system") // У client_credentials нет имени пользователя, поэтому будем использовать system.
                .build()
        );

        String accessToken = client.getAccessToken().getTokenValue();

        ResponseEntity<String> responseEntity = restClient.post()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // Подставляем токен доступа в заголовок Authorization
                .body(operation)
                .retrieve()
                .toEntity(String.class);

        return responseEntity.getBody();
    }*/
}
