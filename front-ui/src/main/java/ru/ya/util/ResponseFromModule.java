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
import ru.ya.dto.TransferDataDto;
import ru.ya.dto.UserDto;
import ru.ya.model.Cash;
import ru.ya.model.CurrencyRates;
import ru.ya.model.NewAccountCurrency;
import ru.ya.model.TransferData;

@Component
public class ResponseFromModule {
    @Value("${spring.application.name}")
    private String moduleName;

    @Value("${module-accounts}")
    private String moduleAccountsHost;
    /*        private String accountsModuleName = "http://accounts";*/

    @Value("${module-cash}")
    private String moduleCashHost;
    /*        private String cashModuleName = "cash";*/

    @Value("${module-transfer}")
    private String moduleTransferHost;
    /*        private String transferModuleName = "transfer";*/

    @Autowired
    OAuth2AuthorizedClientManager manager;

    @Autowired
    RestClient restClient;

    public String getStringResponseFromModuleTransfer(String url, TransferData transferData) {
        return getStringResponseFromModule(moduleTransferHost, url, transferData);
        /*        return getResponseFromModule(transferModuleName, url, transferData);*/
    }

    public String getStringResponseFromModuleCash(String url, Cash cash) {
        return getStringResponseFromModule(moduleCashHost, url, cash);
        /*        return getResponseFromModule(cashModuleName, url, cash);*/
    }

    public String getStringResponseFromModuleAccounts(String url, Object object) {
        return getStringResponseFromModule(moduleAccountsHost, url, object);
        /*        return getResponseFromModule(accountsModuleName, url, object);*/
    }

    private String getStringResponseFromModule(String moduleNameForRequest, String url, Object object) {
        OAuth2AuthorizedClient client = manager.authorize(OAuth2AuthorizeRequest
                .withClientRegistrationId(moduleName)
                .principal("system") // У client_credentials нет имени пользователя, поэтому будем использовать system.
                .build()
        );

        String accessToken = client.getAccessToken().getTokenValue();

        RestClient.RequestBodySpec rCRBS = restClient.post()
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

        ResponseEntity<String> responseEntity = rCRBS
                .retrieve()
                .toEntity(String.class);

        return responseEntity.getBody();
    }

    public CurrencyRates getCurrencyRatesResponseFromModuleExchangeGenerator(String moduleNameForRequest, String url) {
        OAuth2AuthorizedClient client = manager.authorize(OAuth2AuthorizeRequest
                .withClientRegistrationId(moduleName)
                .principal("system") // У client_credentials нет имени пользователя, поэтому будем использовать system.
                .build()
        );

        String accessToken = client.getAccessToken().getTokenValue();

        ResponseEntity<CurrencyRates> responseEntity = restClient.get()
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

        ResponseEntity<UserDto> responseEntity = restClient.get()
                .uri(moduleAccountsHost + userDtoLogin)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // Подставляем токен доступа в заголовок Authorization
                .retrieve().toEntity(UserDto.class);

        return responseEntity.getBody();
    }
}
