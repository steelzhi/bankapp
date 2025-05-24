package ru.ya.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import ru.ya.enums.Roles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(schema = "accounts", name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String login;
    String password;
    String name;
    String surname;
    LocalDate birthdate;

    @Enumerated(EnumType.STRING)
    Roles role;

    @OneToMany
    @JoinColumn(name = "user_id")
    List<BankAccount> bankAccountList = new ArrayList<>();

    public User(String login, String password, String name, String surname, LocalDate birthdate, Roles role, List<BankAccount> bankAccountList) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.birthdate = birthdate;
        this.role = role;
        this.bankAccountList = bankAccountList;
    }

    public User(int id, String login, String password, String name, String surname, LocalDate birthdate, Roles role, List<BankAccount> bankAccountList) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.birthdate = birthdate;
        this.role = role;
        this.bankAccountList = bankAccountList;
    }


    /*    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }*/
}
