package ru.ya.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Operation {
    ru.ya.enums.Operation operation;
    String userLogin;
    String bankAccountNumber;
}
