package ru.ya.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ya.dto.UserDto;
import ru.ya.model.User;
import ru.ya.repository.BankAccountRepository;
import ru.ya.repository.UserRepository;
import ru.ya.service.UserService;

@RestController
public class UserController {
    @Autowired
    UserRepository userAccountRepository;

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    UserService userService;

    @Autowired
    Logger logger;

    @GetMapping("/{login}")
    public UserDto getUser(@PathVariable String login) {
        logger.atInfo().log("Getting user with login = " + login);
        return userService.getUser(login);
    }

/*    @GetMapping("/get-register-form")
    public String getRegisterForm() {
        return "signup";
    }*/


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
    public Boolean editPasswordAndReturnIfEdited(@RequestBody UserDto userDto) {
        logger.atInfo().log("Changing password for user with login = " + userDto.getLogin());
        User userWithEditedPassword = userService.changePassword(userDto);
        if (userWithEditedPassword == null) {
            return false;
        }

        return true;
    }


}
