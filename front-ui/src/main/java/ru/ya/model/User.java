package ru.ya.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.ya.enums.Roles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class User {
    int id;
    String login;
    String password;
    String confirmedPassword;
    String name;
    String surname;
    LocalDate birthdate;
    Roles role;

    public User(String login, String password, String confirmedPassword, String name, String surname, LocalDate birthdate) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.birthdate = birthdate;
        this.confirmedPassword = confirmedPassword;
    }
}
