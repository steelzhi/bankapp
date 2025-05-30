package ru.ya.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CoupleOfValuesDto {
    int senderId;
    String accountNumberFrom;
    String currencyNameFrom;
    double valueFrom;
    int receiverId;
    String accountNumberTo;
    String currencyNameTo;
    double valueTo;
}
