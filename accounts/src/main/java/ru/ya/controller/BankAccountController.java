package ru.ya.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ya.dto.BankAccountDto;
import ru.ya.dto.UserDto;
import ru.ya.enums.Currency;
import ru.ya.model.BankAccount;
import ru.ya.model.NewAccountCurrency;
import ru.ya.model.User;
import ru.ya.repository.BankAccountRepository;
import ru.ya.repository.UserRepository;
import ru.ya.service.BankAccountService;
import ru.ya.service.UserService;

@RestController
public class BankAccountController {
    @Autowired
    BankAccountService bankAccountService;

    @Autowired
    Logger logger;

    @PostMapping("/add-bank-account")
    public Boolean addBankAccountAndReturnIfAdded(@RequestBody NewAccountCurrency newAccountCurrency) {
        logger.atInfo().log("Adding new bank account for user with login = " + newAccountCurrency.getUserDto().getLogin());
        BankAccount addedBankAccount = bankAccountService.addBankAccount(newAccountCurrency);
        if (addedBankAccount == null) {
            return false;
        }

        return true;
    }

    @PostMapping("/delete-bank-account")
    public Boolean deleteBankAccountAndReturnIfDeleted(@RequestBody int id) {
        logger.atInfo().log("Deleting bank account with id = " + id);
        bankAccountService.deleteBankAccountAndReturnIfDeleted(id);
        return true;
    }
}
