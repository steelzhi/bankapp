package ru.ya.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.ya.dto.BankAccountDto;
import ru.ya.dto.UserDto;
import ru.ya.enums.Currency;
import ru.ya.enums.Roles;
import ru.ya.model.TransferData;
import ru.ya.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = FrontUIService.class)
public class FrontUIServiceTest {


    @Autowired
    FrontUIService frontUIService;

    @Test
    void isPasswordCorrect() {
        User user = new User("1", "1", "1", "1", "1", LocalDate.now());
        assertTrue(frontUIService.isUserPasswordCorrect(user), "Passwords don't match");
    }

    @Test
    void isOtherUserDataCorrect() {
        User user = new User("1", "1", "1", "1", "1", LocalDate.now());
        assertFalse(frontUIService.isOtherUserDataCorrect(user), "Data is incorrect");
    }

    @Test
    void isUserAnAdult() {
        User user = new User("1", "1", "1", "1", "1", LocalDate.now());
        assertFalse(frontUIService.isUserAnAdult(user), "User is not an adult");
    }

    @Test
    void areAllUsersBankAccountsEmpty() {
        UserDto userDto = new UserDto(1, "1", "1", "1", "1", LocalDate.now(), Roles.USER);
        List<BankAccountDto> bankAccountDtoList = new ArrayList<>();
        bankAccountDtoList.add(new BankAccountDto(1, "11", 1, Currency.USD));
        userDto.setBankAccountDtoList(bankAccountDtoList);
        assertFalse(frontUIService.areAllUsersBankAccountsEmpty(userDto), "User's bank accounts aren't empty");
    }

    @Test
    void doUsersBankAccountsMatch() {
        TransferData transferData = new TransferData(1, 1, "11", "11", "RUB", 1);
        assertTrue(frontUIService.doUsersBankAccountsMatch(transferData), "User accounts matches");
    }
}
