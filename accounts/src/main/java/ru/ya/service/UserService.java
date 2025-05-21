package ru.ya.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ya.dto.UserDto;
import ru.ya.mapper.UserMapper;
import ru.ya.model.User;
import ru.ya.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userAccountRepository;

    public UserDto getUser(String login) {
        User user = userAccountRepository.findByLogin(login);
        return UserMapper.mapToUserDto(user);
    }

    public boolean doesUserAlreadyExists(UserDto userDto) {
        User user = userAccountRepository.findByLogin(userDto.getLogin());
        if (user != null) {
            return true;
        }
        return false;
    }

    public void addUser(UserDto userDto) {
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodedPassword);
        User user = UserMapper.mapToUser(userDto);
        userAccountRepository.save(user);
    }
}
