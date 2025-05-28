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
import ru.ya.model.CoupleOfValues;
import ru.ya.dto.TransferDataDto;
import ru.ya.model.Operation;
import ru.ya.model.TransferData;

@Component
public class ResponseFromModule {
    @Value("${spring.application.name}")
    private String moduleName;

    @Value("${module-exchange}")
    private String moduleExchangeHost;

    @Value("${module-accounts}")
    private String moduleAccountsHost;
    /*        private String accountsModuleName = "http://accounts";*/

    @Value("${module-notifications}")
    private String moduleNotificationsHost;
    /*    private String notificationsModuleName = "notifications";*/

    @Autowired
    OAuth2AuthorizedClientManager manager;

    @Autowired
    RestClient restClient;
/*
    public CoupleOfValues getCoupleOfValuesResponseFromModuleExchange(String url, TransferDataDto transferDataDto) {
        OAuth2AuthorizedClient client = manager.authorize(OAuth2AuthorizeRequest
                .withClientRegistrationId(moduleName)
                .principal("system") // У client_credentials нет имени пользователя, поэтому будем использовать system.
                .build()
        );

        String accessToken = client.getAccessToken().getTokenValue();

        ResponseEntity<CoupleOfValues> responseEntity = restClient.post()
                .uri(moduleExchangeHost + url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // Подставляем токен доступа в заголовок Authorization
                .body(transferDataDto)
                .retrieve().toEntity(CoupleOfValues.class);

        return responseEntity.getBody();
    }*/

    public TransferDataDto getTransferDataDtoResponseFromModuleAccounts(String url, TransferData transferData) {
        OAuth2AuthorizedClient client = manager.authorize(OAuth2AuthorizeRequest
                .withClientRegistrationId(moduleName)
                .principal("system") // У client_credentials нет имени пользователя, поэтому будем использовать system.
                .build()
        );

        String accessToken = client.getAccessToken().getTokenValue();

        ResponseEntity<TransferDataDto> responseEntity = restClient.post()
                .uri(moduleAccountsHost + url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // Подставляем токен доступа в заголовок Authorization
                .body(transferData)
                .retrieve().toEntity(TransferDataDto.class);

        return responseEntity.getBody();
    }

    public String getStringResponseFromModuleNotifications(String url, Operation operation) {
        OAuth2AuthorizedClient client = manager.authorize(OAuth2AuthorizeRequest
                .withClientRegistrationId(moduleName)
                .principal("system") // У client_credentials нет имени пользователя, поэтому будем использовать system.
                .build()
        );

        String accessToken = client.getAccessToken().getTokenValue();

        ResponseEntity<String> responseEntity = restClient.post()
                .uri(moduleNotificationsHost + url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // Подставляем токен доступа в заголовок Authorization
                .body(operation)
                .retrieve().toEntity(String.class);

        return responseEntity.getBody();
    }
}
