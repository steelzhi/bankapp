package ru.ya.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class User {
    int id;
    String username;
    String password;
    Collection<? extends GrantedAuthority> role;

    List<BankAccount> bankAccountList = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
