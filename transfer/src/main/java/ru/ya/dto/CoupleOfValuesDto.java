package ru.ya.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CoupleOfValuesDto {
    String accountNumberFrom;
    String currencyNameFrom;
    double valueFrom;
    String accountNumberTo;
    String currencyNameTo;
    double valueTo;
}
