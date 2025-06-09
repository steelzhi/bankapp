package ru.ya.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.ya.enums.Currency;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountDto {
    int id;
    int userId;
    String accountNumber;
    double accountValue;
    Currency currency;

    public String getAccountNumber() {
        return accountNumber;
    }

    public Integer getId() {
        return id;
    }

    public BankAccountDto(int userId, String accountNumber, double accountValue, Currency currency) {
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.accountValue = accountValue;
        this.currency = currency;
    }
}
