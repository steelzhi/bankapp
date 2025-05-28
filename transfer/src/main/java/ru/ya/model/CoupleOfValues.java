package ru.ya.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CoupleOfValues {
    String currencyNameFrom;
    double valueFrom;
    String currencyNameTo;
    double valueTo;
}
