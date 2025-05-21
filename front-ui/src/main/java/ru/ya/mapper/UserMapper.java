package ru.ya.mapper;

import ru.ya.dto.UserDto;
import ru.ya.enums.Roles;
import ru.ya.model.User;

public class UserMapper {
    private UserMapper() {
    }

    public static UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getLogin(),
                user.getPassword(),
                user.getName(),
                user.getSurname(),
                user.getBirthdate(),
                Roles.USER);
    }
}
