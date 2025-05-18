package ru.ya.mapper;

import ru.ya.dto.UserDto;
import ru.ya.model.User;

public class UserMapper {
    private UserMapper() {
    }

    public static UserDto mapToUserDto(User user) {
        return new UserDto(user.getUsername(), user.getPassword(), user.getRole());
    }
}
