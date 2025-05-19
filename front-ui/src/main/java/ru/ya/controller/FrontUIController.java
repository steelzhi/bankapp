package ru.ya.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import ru.ya.dto.UserDto;
import ru.ya.service.FrontUIService;

@Controller
public class FrontUIController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FrontUIService frontUIService;

    @Autowired
    OAuth2AuthorizedClientManager manager;

    @GetMapping("/")
    public String getMainPage() {
        /*OAuth2AuthorizedClient client = manager.authorize(OAuth2AuthorizeRequest
                .withClientRegistrationId("front-ui")
                .principal("system") // У client_credentials нет имени пользователя, поэтому будем использовать system.
                .build()
        );
        System.out.println(client.getAccessToken().getTokenValue());

        String accessToken = client.getAccessToken().getTokenValue();

// Клиент для отправки запроса
        RestClient restClient = RestClient.create("http://localhost:8090");

        ResponseEntity<Void> responseEntity = restClient.get()
                .uri("/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // Подставляем токен доступа в заголовок Authorization
                .retrieve()
                .toBodilessEntity();
        System.out.println(responseEntity.getStatusCode());*/

        return "entry";
    }

    @GetMapping("/signup")
    public String register() {
        return "signup";
    }

    @GetMapping("/account")
    public String enterToCab() {

/*        ResponseEntity<UserDto> response = restTemplate.getForEntity("http://localhost:8090/", UserDto.class);
        if (response != null) {
            return "account";
        } else {
            return "entry";
        }*/



        return "account";
    }
}
