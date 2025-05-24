package ru.ya.service;

import org.hibernate.Session;
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
    UserRepository userRepository;

    public UserDto getUser(String login) {
        User user = userRepository.findByLogin(login);
        return UserMapper.mapToUserDto(user);
    }

    public boolean doesUserAlreadyExists(UserDto userDto) {
        User user = userRepository.findByLogin(userDto.getLogin());
        if (user != null) {
            return true;
        }
        return false;
    }

    public User addUser(UserDto userDto) {
        UserDto userDtoWithEncodedPassword = getUserDtoWithEncodedPassword(userDto);
        User user = UserMapper.mapToUser(userDtoWithEncodedPassword);
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    public User changePasswordAndReturnIfChanged(UserDto userDto) {
        User userWithChangedPassword = addUser(userDto);
        return userWithChangedPassword;
    }

    public User changeOtherDataAndReturnIfChanged(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        User userWithChangedData = userRepository.save(user);
        return userWithChangedData;
    }

    public void deleteUser(UserDto userDto) {
        User user = userRepository.findByLogin(userDto.getLogin());
        userRepository.deleteById(user.getId());
        //userRepository.deleteUserByLogin(userDto.getLogin());
/*        userRepository.deleteById(userId);*/
    }

    private UserDto getUserDtoWithEncodedPassword(UserDto userDto) {
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodedPassword);
        return userDto;
    }
}
