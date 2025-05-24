package ru.ya.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import ru.ya.dto.UserDto;
import ru.ya.mapper.UserMapper;
import ru.ya.model.User;
import ru.ya.model.UserPrincipal;
import ru.ya.service.FrontUIService;

@Controller
public class FrontUIController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FrontUIService frontUIService;

    @Autowired
    OAuth2AuthorizedClientManager manager;

    @Autowired
    UserDetailsService userDetailsService;


    @GetMapping("/")
    public String getMainPage() {
        return "main-page";
    }

    @GetMapping("/get-register-form")
    public String getRegisterForm() {
        return "register-form";
    }

    @PostMapping("/register-user")
    public String registerUser(Model model, @ModelAttribute User user) {
        model.addAttribute("login", user.getLogin());
        if (!frontUIService.isUserPasswordCorrect(user)) {
            return "user-password-was-not-confirmed.html";
        }
        if (!frontUIService.isUserAnAdult(user)) {
            return "user-is-not-an-adult.html";
        }

        UserDto userDto = UserMapper.mapToUserDto(user);
        ResponseEntity<Boolean> responseEntityFromModuleAccounts = getResponseEntityFromModuleAccounts("/register-user", userDto);

        if (responseEntityFromModuleAccounts.getBody()) {
            return "user-registered-successfully.html";
        } else {
            return "user-already-exists.html";
        }
    }

    @GetMapping("/account")
    public String enterToAccount(Model model) {
        String login = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            login = userDetails.getUsername();
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(login);
        UserPrincipal userPrincipal = (UserPrincipal) userDetails;
        UserDto userDto = userPrincipal.getUserDto();

        model.addAttribute("user", userDto);

        return "account";
    }

    @PostMapping("/user/{login}/edit-password")
    public String editPassword(Model model, @ModelAttribute User user) {
        model.addAttribute("login", user.getLogin());
        if (!frontUIService.isUserPasswordCorrect(user)) {
            return "user-password-was-not-confirmed.html";
        }

        UserDto userDto = UserMapper.mapToUserDto(user);
        ResponseEntity<Boolean> responseEntityFromModuleAccounts = getResponseEntityFromModuleAccounts("/edit-password", userDto);

        if (responseEntityFromModuleAccounts.getBody()) {
            return "password-changed-successfully.html";
        } else {
            return "user-already-exists.html";
        }
    }

    @PostMapping("/user/{login}/edit-other-data")
    public String editOtherData(Model model, @ModelAttribute User user) {
        model.addAttribute("login", user.getLogin());
        if (!frontUIService.isOtherUserDataCorrect(user)) {
            return "user-data-is-incorrect.html";
        }

        UserDto userDto = UserMapper.mapToUserDto(user);
        ResponseEntity<Boolean> responseEntityFromModuleAccounts = getResponseEntityFromModuleAccounts("/edit-other-data", userDto);

        if (responseEntityFromModuleAccounts.getBody()) {
            return "user-data-changed-successfully.html";
        } else {
            return "user-already-exists.html";
        }
    }

    private ResponseEntity<Boolean> getResponseEntityFromModuleAccounts(String url, UserDto userDto) {
        RestClient restClient = RestClient.create("http://localhost:8090");
        OAuth2AuthorizedClient client = manager.authorize(OAuth2AuthorizeRequest
                .withClientRegistrationId("front-ui")
                .principal("system") // У client_credentials нет имени пользователя, поэтому будем использовать system.
                .build()
        );

        String accessToken = client.getAccessToken().getTokenValue();

        ResponseEntity<Boolean> responseEntity = restClient.post()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // Подставляем токен доступа в заголовок Authorization
                .body(userDto)
                .retrieve()
                .toEntity(Boolean.class);

        return responseEntity;
    }
}
