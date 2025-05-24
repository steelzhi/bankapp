package ru.ya.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import ru.ya.enums.Roles;
import ru.ya.model.BankAccount;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserDto {
    int id;
    String login;
    String password;
    String name;
    String surname;
    LocalDate birthdate;
    Roles role;
    List<BankAccountDto> bankAccountDtoList = new ArrayList<>();
}
