package ru.ya.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

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

    BigInteger accountNumber;
    int accountValue;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    User user;

    public BankAccount(BigInteger accountNumber, int accountValue) {
        this.accountNumber = accountNumber;
        this.accountValue = accountValue;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
               "accountValue=" + accountValue +
               ", accountNumber=" + accountNumber +
               ", id=" + id +
               '}';
    }
}
