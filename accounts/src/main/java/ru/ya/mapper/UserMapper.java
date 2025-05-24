package ru.ya.mapper;

import ru.ya.dto.BankAccountDto;
import ru.ya.dto.UserDto;
import ru.ya.enums.Roles;
import ru.ya.model.BankAccount;
import ru.ya.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    private UserMapper() {
    }

    public static UserDto mapToUserDto(User user) {
        List<BankAccount> bankAccountList = user.getBankAccountList();
        List<BankAccountDto> bankAccountDtoList = BankAccountMapper.mapToBankAccountDtoList(bankAccountList);

        return new UserDto(
                user.getId(),
                user.getLogin(),
                user.getPassword(),
                user.getName(),
                user.getSurname(),
                user.getBirthdate(),
                Roles.USER,
                bankAccountDtoList);
    }

    public static User mapToUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getLogin(),
                userDto.getPassword(),
                userDto.getName(),
                userDto.getSurname(),
                userDto.getBirthdate(),
                Roles.USER,
                new ArrayList<>());
    }
}
