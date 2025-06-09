package ru.ya.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.ya.enums.Currency;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class BankAccountDto {
    int id;
    int userId;
    String accountNumber;
    double accountValue;
    Currency currency;
}
