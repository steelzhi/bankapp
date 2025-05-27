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
import ru.ya.model.Cash;
import ru.ya.model.Operation;

@Component
public class ResponseFromModule {
    @Value("${spring.application.name}")
    private String moduleName;

    @Value("${module-accounts}")
    private String moduleAccountsHost;
    /*        private String accountsModuleName = "accounts";*/

    @Value("${module-notifications}")
    private String moduleNotificationsHost;
    /*        private String notificationsModuleName = "notifications";*/

    @Value("${module-blocker}")
    private String moduleBlockerHost;
    /*        private String blockerModuleName = "blocker";*/

    @Autowired
    OAuth2AuthorizedClientManager manager;

    @Autowired
    RestClient restClient;

    public String getStringResponseFromModuleAccounts(String url, Cash cash) {
        /*        return getResponseFromModule(accountsModuleName, url, cash);*/
        return getStringResponseFromModule(moduleAccountsHost, url, cash);
    }

    public String getStringResponseForSuccessfullOpFromModuleNotifications(String url, Operation operation) {
        /*        return getResponseFromModule(notificationsModuleName, url, operation);*/
        return getStringResponseFromModule(moduleNotificationsHost, url, operation);
    }

    public Boolean getBooleanResponseForDecreaseOpFromModuleAccounts(String url, Cash cash) {
        return getBooleanResponseFromModule(moduleAccountsHost, url, cash);

/*        RestClient.RequestBodySpec rCRBS = getRestClientRequestBodySpecWithAccessToken(moduleAccountsHost, url);

        ResponseEntity<Boolean> responseEntity = rCRBS
                .body(cash)
                .retrieve()
                .toEntity(Boolean.class);

        return responseEntity.getBody();*/
    }

    public Boolean getBooleanResponseForSuspiciousOpsFromModuleBlocker(String url) {
        return getBooleanResponseFromModule(moduleBlockerHost, url, moduleName);

/*        RestClient.RequestBodySpec rCRBS = getRestClientRequestBodySpecWithAccessToken(moduleBlockerHost, url);

        ResponseEntity<Boolean> responseEntity = rCRBS
                .body(moduleName)
                .retrieve()
                .toEntity(Boolean.class);

        return responseEntity.getBody();*/
    }

    private Boolean getBooleanResponseFromModule(String moduleNameForRequest, String url, Object object) {
        RestClient.RequestBodySpec rCRBS = getRestClientRequestBodySpecWithAccessToken(moduleNameForRequest, url);

        if (object instanceof Cash cash) {
            rCRBS.body(cash);
        } else {
            rCRBS.body(moduleName);
        }

        ResponseEntity<Boolean> responseEntity = rCRBS
                .retrieve()
                .toEntity(Boolean.class);

        return responseEntity.getBody();
    }

    private String getStringResponseFromModule(String moduleNameForRequest, String url, Object object) {
        RestClient.RequestBodySpec rCRBS = getRestClientRequestBodySpecWithAccessToken(moduleNameForRequest, url);

        if (object instanceof Operation operation) {
            rCRBS.body(operation);
        } else if (object instanceof Cash cash) {
            rCRBS.body(cash);
        }

        ResponseEntity<String> responseEntity = rCRBS
                .retrieve()
                .toEntity(String.class);

        return responseEntity.getBody();
    }

    private RestClient.RequestBodySpec getRestClientRequestBodySpecWithAccessToken(String moduleNameForRequest, String url) {
        OAuth2AuthorizedClient client = manager.authorize(OAuth2AuthorizeRequest
                .withClientRegistrationId(moduleName)
                .principal("system") // У client_credentials нет имени пользователя, поэтому будем использовать system.
                .build()
        );

        String accessToken = client.getAccessToken().getTokenValue();
        RestClient.RequestBodySpec rCRBS = restClient.post()
                .uri(moduleNameForRequest + url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken); // Подставляем токен доступа в заголовок Authorization

        return rCRBS;
    }
}
