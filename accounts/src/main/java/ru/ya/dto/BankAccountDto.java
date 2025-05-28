package ru.ya.dto;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.ya.enums.Currency;
import ru.ya.model.User;

import java.math.BigInteger;

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
