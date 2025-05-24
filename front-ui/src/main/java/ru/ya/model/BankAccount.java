package ru.ya.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.ya.dto.UserDto;
import ru.ya.enums.Currency;

import java.math.BigInteger;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class BankAccount {
    int id;
    UserDto userDto;
    int accountNumber;
    int accountValue;
    Currency currency;

    public BankAccount(UserDto userDto, Currency currency) {
        this.userDto = userDto;
        this.currency = currency;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public int getAccountValue() {
        return accountValue;
    }
}
