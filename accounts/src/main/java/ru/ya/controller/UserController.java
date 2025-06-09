package ru.ya.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.ya.dto.UserDto;
import ru.ya.enums.ErrorOperation;
import ru.ya.enums.SuccessfullOperation;
import ru.ya.model.Operation;
import ru.ya.service.UserService;
import ru.ya.util.ResponseFromModule;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    Logger logger;

    @Autowired
    ResponseFromModule responseFromModule;

    @GetMapping("/{login}")
    public UserDto getUser(@PathVariable String login) {
        logger.atInfo().log("Getting user with login = " + login);
        return userService.getUserDto(login);
    }

    @GetMapping("/users-except/{login}")
    public List<UserDto> getOtherUsers(@PathVariable String login) {
        logger.atInfo().log("Getting all users except user with login = " + login);
        return userService.getOtherUserDtos(login);
    }

    @PostMapping(value = "/register-user", consumes = {MediaType.APPLICATION_JSON_VALUE})
    // параметр "consumes" нужен для тестов - иначе в тестах не распознается формат application/json
    public String registerUser(@RequestBody UserDto userDto) {
        if (userService.doesUserAlreadyExists(userDto)) {
            return responseFromModule.getResponseFromModuleNotifications("/notification/error", new Operation(ErrorOperation.USER_ALREADY_EXISTS, userDto.getLogin()));
        }

        logger.atInfo().log("Adding user with login = " + userDto.getLogin());
        userService.addUser(userDto);
        return responseFromModule.getResponseFromModuleNotifications("/notification/success", new Operation(SuccessfullOperation.USER_CREATING, userDto.getLogin()));
    }

    @PostMapping("/edit-password")
    public String editPassword(@RequestBody UserDto userDto) {
        logger.atInfo().log("Changing password for user with login = " + userDto.getLogin());
        userService.changePasswordAndReturnIfChanged(userDto);
        return responseFromModule.getResponseFromModuleNotifications("/notification/success", new Operation(SuccessfullOperation.PASSWORD_EDITING, userDto.getLogin()));
    }

    @PostMapping("/edit-other-data")
    public String editOtherData(@RequestBody UserDto userDto) {
        logger.atInfo().log("Changing other data for user with login = " + userDto.getLogin());
        userService.changeOtherDataAndReturnIfChanged(userDto);
        return responseFromModule.getResponseFromModuleNotifications("/notification/success", new Operation(SuccessfullOperation.OTHER_DATA_EDITING, userDto.getLogin()));
    }

    @PostMapping("/delete-user")
    public void deleteUser(@RequestBody UserDto userDto) {
        logger.atInfo().log("Deleting user with login = " + userDto.getLogin());
        userService.deleteUser(userDto);
    }
}
