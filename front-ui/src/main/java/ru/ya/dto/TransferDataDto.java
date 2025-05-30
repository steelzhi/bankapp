package ru.ya.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class TransferDataDto {
    int senderId;
    int receiverId;
    String accountNumberFrom;
    String currencyNameFrom;
    String accountNumberTo;
    String currencyNameTo;
    double sum;
}
