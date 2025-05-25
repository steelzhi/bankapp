package ru.ya.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import ru.ya.dto.UserDto;
import ru.ya.model.Operation;
import ru.ya.model.User;
import ru.ya.service.UserService;

@RestController
public class UserController {
    @Value("${spring.application.name}")
    private String moduleName;

    @Value("${module-notifications}")
    private String moduleNotificationsHost;

    @Autowired
    UserService userService;

    @Autowired
    Logger logger;

    @Autowired
    OAuth2AuthorizedClientManager manager;

    @GetMapping("/{login}")
    public UserDto getUser(@PathVariable String login) {
        logger.atInfo().log("Getting user with login = " + login);
        return userService.getUserDto(login);
    }

    @PostMapping("/register-user")
    public Boolean registerUserAndReturnIfRegistered(@RequestBody UserDto userDto) {
        if (userService.doesUserAlreadyExists(userDto)) {
            return false;
        }

        logger.atInfo().log("Adding user with login = " + userDto.getLogin());
        userService.addUser(userDto);
        requestNotificationFromModuleNotifications("/notification", new Operation(ru.ya.enums.Operation.USER_CREATING, userDto.getLogin(), null));

        return true;
    }

    @PostMapping("/edit-password")
    public Boolean editPasswordAndReturnEditedUser(@RequestBody UserDto userDto) {
        logger.atInfo().log("Changing password for user with login = " + userDto.getLogin());
        User userWithChangedPassword = userService.changePasswordAndReturnIfChanged(userDto);
        if (userWithChangedPassword == null) {
            return false;
        }

        return true;
    }

    @PostMapping("/edit-other-data")
    public Boolean editOtherDataAndReturnEditedUser(@RequestBody UserDto userDto) {
        logger.atInfo().log("Changing other data for user with login = " + userDto.getLogin());
        User userWithChangedData = userService.changeOtherDataAndReturnIfChanged(userDto);
        if (userWithChangedData == null) {
            return false;
        }

        return true;
    }

    @PostMapping("/delete-user")
    public void deleteUser(@RequestBody UserDto userDto) {
        logger.atInfo().log("Deleting user with login = " + userDto.getLogin());
        userService.deleteUser(userDto);
        //deleteUser(userId);
    }

    private void requestNotificationFromModuleNotifications(String url, Operation operation) {
        RestClient restClient = RestClient.create(moduleNotificationsHost);
        OAuth2AuthorizedClient client = manager.authorize(OAuth2AuthorizeRequest
                .withClientRegistrationId(moduleName)
                .principal("system") // У client_credentials нет имени пользователя, поэтому будем использовать system.
                .build()
        );

        String accessToken = client.getAccessToken().getTokenValue();

        restClient.post()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // Подставляем токен доступа в заголовок Authorization
                    .body(operation)
                    .retrieve()
                    .toBodilessEntity();
    }
}
