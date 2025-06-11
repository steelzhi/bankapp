package ru.ya.util;

import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.ya.dto.UserDto;
import ru.ya.model.Cash;
import ru.ya.model.CurrencyRates;
import ru.ya.model.NewAccountCurrency;
import ru.ya.model.TransferData;

import java.util.List;

@Component
public class ResponseFromModule {
    @Value("${spring.application.name}")
    private String moduleName;

    @Value("${module-accounts}")
    private String moduleAccountsHost;

    @Value("${module-cash}")
    private String moduleCashHost;

    @Value("${module-transfer}")
    private String moduleTransferHost;

    @Autowired
    OAuth2AuthorizedClientManager manager;

    @Autowired
    private RestClient.Builder restClientBuilder;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    public String getStringResponseFromModuleTransfer(String url, TransferData transferData) {
        return getStringResponseFromModule(moduleTransferHost, url, transferData);
    }

    public String getStringResponseFromModuleCash(String url, Cash cash) {
        return getStringResponseFromModule(moduleCashHost, url, cash);
    }

    public String getStringResponseFromModuleAccounts(String url, Object object) {
        return getStringResponseFromModule(moduleAccountsHost, url, object);
    }

    @Retry(name = "responseFromModule", fallbackMethod = "getFallback")
    private String getStringResponseFromModule(String moduleNameForRequest, String url, Object object) {
        OAuth2AuthorizedClient client = manager.authorize(OAuth2AuthorizeRequest
                .withClientRegistrationId(moduleName)
                .principal("system") // У client_credentials нет имени пользователя, поэтому будем использовать system.
                .build()
        );

        String accessToken = client.getAccessToken().getTokenValue();

        RestClient.RequestBodySpec rCRBS = restClientBuilder.build().post()
                .uri(moduleNameForRequest + url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken); // Подставляем токен доступа в заголовок Authorization

        if (object instanceof UserDto userDto) {
            rCRBS.body(userDto);
        } else if (object instanceof NewAccountCurrency newAccountCurrency) {
            rCRBS.body(newAccountCurrency);
        } else if (object instanceof Integer id) {
            rCRBS.body(id);
        } else if (object instanceof Cash cash) {
            rCRBS.body(cash);
        } else if (object instanceof TransferData transferData) {
            rCRBS.body(transferData);
        }

/*        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
        String ans = circuitBreaker.run(() -> rCRBS
                .retrieve()
                .toEntity(String.class)
                .getBody(), throwable -> getFallback(throwable));

        return ans;*/
        String ans = rCRBS
                .retrieve()
                .toEntity(String.class)
                .getBody();

        return ans;
    }

    public CurrencyRates getCurrencyRatesResponseFromModuleExchangeGenerator(String moduleNameForRequest, String url) {
        OAuth2AuthorizedClient client = manager.authorize(OAuth2AuthorizeRequest
                .withClientRegistrationId(moduleName)
                .principal("system") // У client_credentials нет имени пользователя, поэтому будем использовать system.
                .build()
        );

        String accessToken = client.getAccessToken().getTokenValue();

        ResponseEntity<CurrencyRates> responseEntity = restClientBuilder.build().get()
                .uri(moduleNameForRequest + url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // Подставляем токен доступа в заголовок Authorization
                .retrieve().toEntity(CurrencyRates.class);

        return responseEntity.getBody();
    }


    public UserDto getUserDtoResponseFromModuleAccounts(String userDtoLogin) {
        OAuth2AuthorizedClient client = manager.authorize(OAuth2AuthorizeRequest
                .withClientRegistrationId(moduleName)
                .principal("system") // У client_credentials нет имени пользователя, поэтому будем использовать system.
                .build()
        );

        String accessToken = client.getAccessToken().getTokenValue();

        ResponseEntity<UserDto> responseEntity = restClientBuilder.build().get()
                .uri(moduleAccountsHost + userDtoLogin)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // Подставляем токен доступа в заголовок Authorization
                .retrieve().toEntity(UserDto.class);

        return responseEntity.getBody();
    }

    public List<UserDto> getUserDtoListResponseFromModuleAccounts(String userDtoLogin) {
        OAuth2AuthorizedClient client = manager.authorize(OAuth2AuthorizeRequest
                .withClientRegistrationId(moduleName)
                .principal("system") // У client_credentials нет имени пользователя, поэтому будем использовать system.
                .build()
        );

        String accessToken = client.getAccessToken().getTokenValue();

        List<UserDto> userDtoList = restClientBuilder.build().get()
                .uri(moduleAccountsHost + "users-except/" + userDtoLogin)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // Подставляем токен доступа в заголовок Authorization
                .retrieve()
                .body(new ParameterizedTypeReference<List<UserDto>>() {
                });

        return userDtoList;
    }

    private String getFallback(Throwable throwable) {
        System.out.println("Fallback executed. Error: " + throwable.getMessage());
        return new String("service-is-unavailable");
    }
}
