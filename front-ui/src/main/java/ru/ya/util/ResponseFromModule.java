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
import ru.ya.dto.UserDto;
import ru.ya.model.NewAccountCurrency;

@Component
public class ResponseFromModule {
    @Value("${spring.application.name}")
    private String moduleName;

    @Value("${module-accounts}")
    private String moduleAccountsHost;

    @Autowired
    OAuth2AuthorizedClientManager manager;

    public String getResponseFromModuleAccounts(String url, Object object) {
        RestClient restClient = RestClient.create(moduleAccountsHost);
        OAuth2AuthorizedClient client = manager.authorize(OAuth2AuthorizeRequest
                .withClientRegistrationId(moduleName)
                .principal("system") // У client_credentials нет имени пользователя, поэтому будем использовать system.
                .build()
        );

        String accessToken = client.getAccessToken().getTokenValue();

        ResponseEntity<String> responseEntity = null;
        if (object instanceof UserDto userDto) {
            responseEntity = restClient.post()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // Подставляем токен доступа в заголовок Authorization
                    .body(userDto)
                    .retrieve()
                    .toEntity(String.class);
        } else if (object instanceof NewAccountCurrency newAccountCurrency) {
            responseEntity = restClient.post()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // Подставляем токен доступа в заголовок Authorization
                    .body(newAccountCurrency)
                    .retrieve()
                    .toEntity(String.class);
        } else if (object instanceof Integer id) {
            responseEntity = restClient.post()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // Подставляем токен доступа в заголовок Authorization
                    .body(id)
                    .retrieve()
                    .toEntity(String.class);
        }

        return responseEntity.getBody();
    }
}
