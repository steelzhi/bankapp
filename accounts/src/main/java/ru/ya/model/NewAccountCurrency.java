package ru.ya.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.ya.dto.UserDto;
import ru.ya.enums.Currency;

@Data
@AllArgsConstructor
public class NewAccountCurrency {
    private Currency currency;
    private UserDto userDto;
}
