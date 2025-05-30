package ru.ya.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.ya.enums.Roles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserDtos {
    private List<UserDto> userDtoList;

    public List<BankAccountDto> getUserBankAccountDtos(int userId) {
        for (UserDto userDto : userDtoList) {
            if (userDto.getId() == userId) {
                return userDto.getBankAccountDtoList();
            }
        }

        return null;
    }

    public List<UserDto> getUserDtoList() {
        return userDtoList;
    }
}
