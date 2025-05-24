package ru.ya.mapper;

import ru.ya.dto.BankAccountDto;
import ru.ya.dto.UserDto;
import ru.ya.enums.Roles;
import ru.ya.model.BankAccount;
import ru.ya.model.User;

import java.util.ArrayList;
import java.util.List;

public class BankAccountMapper {
    private BankAccountMapper() {
    }

    public static BankAccountDto mapToBankAccountDto(BankAccount bankAccount) {
        return new BankAccountDto(
                bankAccount.getId(),
                bankAccount.getUser().getId(),
                bankAccount.getAccountNumber(),
                bankAccount.getAccountValue(),
                bankAccount.getCurrency()
        );
    }

    public static List<BankAccountDto> mapToBankAccountDtoList(List<BankAccount> bankAccountList) {
        List<BankAccountDto> bankAccountDtoList = new ArrayList<>();
        for (BankAccount bankAccount : bankAccountList) {
            bankAccountDtoList.add(mapToBankAccountDto(bankAccount));
        }

        return bankAccountDtoList;
    }
}
