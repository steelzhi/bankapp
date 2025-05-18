package ru.ya.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigInteger;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class BankAccount {
    int id;

    BigInteger accountNumber;
    int accountValue;

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
