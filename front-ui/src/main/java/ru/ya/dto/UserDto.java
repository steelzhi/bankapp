package ru.ya.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import ru.ya.enums.Roles;

import java.time.LocalDate;
import java.util.Collection;

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
}
