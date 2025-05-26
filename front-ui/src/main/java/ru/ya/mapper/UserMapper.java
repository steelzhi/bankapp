package ru.ya.mapper;

import ru.ya.dto.BankAccountDto;
import ru.ya.dto.UserDto;
import ru.ya.enums.Roles;
import ru.ya.model.User;

import java.util.List;

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

/*    public static UserDto mapToUserDto(User user, List<BankAccountDto> bankAccountDtoList) {
        UserDto userDto = new UserDto(
                user.getId(),
                user.getLogin(),
                user.getPassword(),
                user.getName(),
                user.getSurname(),
                user.getBirthdate(),
                Roles.USER);
        userDto.setBankAccountDtoList(bankAccountDtoList);
        return userDto;
    }*/
}
