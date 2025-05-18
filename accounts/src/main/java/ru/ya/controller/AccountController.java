package ru.ya.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.ya.dto.UserDto;
import ru.ya.mapper.UserMapper;
import ru.ya.model.User;
import ru.ya.repository.BankAccountRepository;
import ru.ya.repository.UserAccountRepository;

@RestController
public class AccountController {
    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    BankAccountRepository bankAccountRepository;

    @GetMapping("/{username}")
    public UserDto getUser(@PathVariable String username) {
        User user = userAccountRepository.findByUsername(username);
        return UserMapper.mapToUserDto(user);
    }


}
