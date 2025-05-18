package ru.ya.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import ru.ya.enums.Roles;

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
    String username;
    String password;

    @Enumerated(EnumType.STRING)
    Roles role;

    @OneToMany
    @JoinColumn(name = "user_id")
    List<BankAccount> bankAccountList = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
