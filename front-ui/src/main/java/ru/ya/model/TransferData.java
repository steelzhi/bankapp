package ru.ya.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.ya.enums.Currency;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class TransferData {
    int senderId;
    int receiverId;
    String accountNumberFrom;
    String accountNumberTo;
    String currencyNameTo;
    double sum;
}
