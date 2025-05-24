package ru.ya.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ya.dto.UserDto;
import ru.ya.model.User;
import ru.ya.service.UserService;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    Logger logger;

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
}
