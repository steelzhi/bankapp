package ru.ya.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ya.dto.UserDto;
import ru.ya.mapper.UserMapper;
import ru.ya.model.User;
import ru.ya.repository.BankAccountRepository;
import ru.ya.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BankAccountRepository bankAccountRepository;

    public UserDto getUserDto(String login) {
        User user = userRepository.findByLogin(login);
        return UserMapper.mapToUserDto(user);
    }

    public List<UserDto> getOtherUserDtos(String login) {
        List<User> users = userRepository.findAllOtherUsersExceptUser(login);
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : users) {
            userDtoList.add(UserMapper.mapToUserDto(user));
        }

        return userDtoList;
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
        UserDto userDtoWithEncodedPassword = getUserDtoWithEncodedPassword(userDto);
        userRepository.changePassword(userDtoWithEncodedPassword.getPassword(), userDtoWithEncodedPassword.getId());
        return userRepository.findById(userDtoWithEncodedPassword.getId()).get();
    }

    public User changeOtherDataAndReturnIfChanged(UserDto userDto) {
        userRepository.changeOtherData(userDto.getName(), userDto.getSurname(), userDto.getBirthdate(), userDto.getId());
        return userRepository.findById(userDto.getId()).get();
    }

    public void deleteUser(UserDto userDto) {
        User user = userRepository.findByLogin(userDto.getLogin());
        userRepository.deleteById(user.getId());
    }

    public UserDto getUserDtoWithEncodedPassword(UserDto userDto) {
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodedPassword);
        return userDto;
    }
}
