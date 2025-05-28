package ru.ya.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.ya.enums.Currency;

import java.math.BigInteger;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(schema = "accounts", name = "bank_accounts")
@NoArgsConstructor
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String accountNumber;
    double accountValue;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Enumerated(EnumType.STRING)
    Currency currency;

    public BankAccount(User user, String accountNumber, double accountValue, Currency currency) {
        this.user = user;
        this.accountNumber = accountNumber;
        this.accountValue = accountValue;
        this.currency = currency;
    }

    public BankAccount(User user, int accountValue, Currency currency) {
        this.user = user;
        this.accountValue = accountValue;
        this.currency = currency;
    }
}
