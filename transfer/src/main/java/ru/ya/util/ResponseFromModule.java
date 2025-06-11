package ru.ya.util;

import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.ya.dto.CoupleOfValuesDto;
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

    @Value("${module-notifications}")
    private String moduleNotificationsHost;

    @Value("${module-blocker}")
    private String moduleBlockerHost;

    @Autowired
    OAuth2AuthorizedClientManager manager;

    @Autowired
    private RestClient.Builder restClientBuilder;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    public CoupleOfValues getCoupleOfValuesResponseFromModuleExchange(String url, TransferDataDto transferDataDto) {
        OAuth2AuthorizedClient client = manager.authorize(OAuth2AuthorizeRequest
                .withClientRegistrationId(moduleName)
                .principal("system") // У client_credentials нет имени пользователя, поэтому будем использовать system.
                .build()
        );

        String accessToken = client.getAccessToken().getTokenValue();

        ResponseEntity<CoupleOfValues> responseEntity = restClientBuilder.build().post()
                .uri(moduleExchangeHost + url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // Подставляем токен доступа в заголовок Authorization
                .body(transferDataDto)
                .retrieve().toEntity(CoupleOfValues.class);

        return responseEntity.getBody();
    }

    public TransferDataDto getTransferDataDtoResponseFromModuleAccounts(String url, TransferData transferData) {
        OAuth2AuthorizedClient client = manager.authorize(OAuth2AuthorizeRequest
                .withClientRegistrationId(moduleName)
                .principal("system") // У client_credentials нет имени пользователя, поэтому будем использовать system.
                .build()
        );

        String accessToken = client.getAccessToken().getTokenValue();

        ResponseEntity<TransferDataDto> responseEntity = restClientBuilder.build().post()
                .uri(moduleAccountsHost + url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // Подставляем токен доступа в заголовок Authorization
                .body(transferData)
                .retrieve().toEntity(TransferDataDto.class);

        return responseEntity.getBody();
    }

    public String getStringResponseFromModuleNotifications(String url, Operation operation) {
        return getStringResponseFromModule(moduleNotificationsHost, url, operation);
    }

    public String getStringResponseFromModuleAccounts(String url, CoupleOfValuesDto coupleOfValuesDto) {
        return getStringResponseFromModule(moduleAccountsHost, url, coupleOfValuesDto);
    }

    public Boolean getBooleanResponseForSuspiciousOpsFromModuleBlocker(String url) {
        return getBooleanResponseFromModule(moduleBlockerHost, url);
    }

    public Boolean getBooleanResponseFromModuleAccounts(String url, TransferData transferData) {
        ResponseEntity<Boolean> responseEntity = getRestClientRequestBodySpecWithAccessToken(moduleAccountsHost, url)
                .body(transferData)
                .retrieve()
                .toEntity(Boolean.class);

        return responseEntity.getBody();
    }

    private Boolean getBooleanResponseFromModule(String moduleNameForRequest, String url) {
        ResponseEntity<Boolean> responseEntity = getRestClientRequestBodySpecWithAccessToken(moduleNameForRequest, url)
                .body(moduleName)
                .retrieve()
                .toEntity(Boolean.class);

        return responseEntity.getBody();
    }

    @Retry(name = "responseFromModule", fallbackMethod = "getFallback")
    private String getStringResponseFromModule(String moduleNameForRequest, String url, Object object) {
        RestClient.RequestBodySpec rCRBS = getRestClientRequestBodySpecWithAccessToken(moduleNameForRequest, url);

        if (object instanceof Operation operation) {
            rCRBS.body(operation);
        } else if (object instanceof CoupleOfValuesDto coupleOfValuesDto) {
            rCRBS.body(coupleOfValuesDto);
        }

/*        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
        String ans = circuitBreaker.run(() -> rCRBS
                .retrieve()
                .toEntity(String.class)
                .getBody(), throwable -> getFallback(throwable));*/
        String ans = rCRBS
                .retrieve()
                .toEntity(String.class)
                .getBody();

        return ans;
    }

    private RestClient.RequestBodySpec getRestClientRequestBodySpecWithAccessToken(String moduleNameForRequest, String url) {
        OAuth2AuthorizedClient client = manager.authorize(OAuth2AuthorizeRequest
                .withClientRegistrationId(moduleName)
                .principal("system") // У client_credentials нет имени пользователя, поэтому будем использовать system.
                .build()
        );

        String accessToken = client.getAccessToken().getTokenValue();
        RestClient.RequestBodySpec rCRBS = restClientBuilder.build().post()
                .uri(moduleNameForRequest + url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken); // Подставляем токен доступа в заголовок Authorization

        return rCRBS;
    }

    private String getFallback(Throwable throwable) {
        System.out.println("Fallback executed. Error: " + throwable.getMessage());
        return new String("service-is-unavailable");
    }
}
