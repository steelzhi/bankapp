package ru.ya.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ya.enums.ErrorOperation;
import ru.ya.enums.SuccessfullOperation;
import ru.ya.model.*;
import ru.ya.service.BankAccountService;
import ru.ya.util.ResponseFromModule;

@RestController
public class BankAccountController {
    @Autowired
    BankAccountService bankAccountService;

    @Autowired
    Logger logger;

    @Autowired
    ResponseFromModule responseFromModule;

    @PostMapping("/add-bank-account")
    public String addBankAccount(@RequestBody NewAccountCurrency newAccountCurrency) {
        logger.atInfo().log("Adding new bank account for user with login = " + newAccountCurrency.getUserDto().getLogin());
        BankAccount addedBankAccount = bankAccountService.addBankAccount(newAccountCurrency);
        if (addedBankAccount == null) {
            //return getResponseFromModuleNotifications("/notification/error", new Operation(ErrorOperation.BANK_ACCOUNT_ALREADY_EXISTS));
            return responseFromModule.getResponseFromModuleNotifications("/notification/error", new Operation(ErrorOperation.BANK_ACCOUNT_ALREADY_EXISTS));

        }

        //return getResponseFromModuleNotifications("/notification/success", new Operation(SuccessfullOperation.BANK_ACCOUNT_CREATING));
        return responseFromModule.getResponseFromModuleNotifications("/notification/success", new Operation(SuccessfullOperation.BANK_ACCOUNT_CREATING));
    }

    @PostMapping("/delete-bank-account")
    public String deleteBankAccount(@RequestBody int id) {
        logger.atInfo().log("Deleting bank account with id = " + id);
        bankAccountService.deleteBankAccountAndReturnIfDeleted(id);
        return responseFromModule.getResponseFromModuleNotifications("/notification/success", new Operation(SuccessfullOperation.BANK_ACCOUNT_DELETING));
    }

    @PostMapping("/increase-sum")
    public void increaseSumOnBankAccountAndReturnSuccessStatus(@RequestBody Cash cash) {
        bankAccountService.increaseSumOnBankAccount(cash);
    }

    @PostMapping("/decrease-sum")
    public Boolean decreaseSumOnBankAccountAndReturnSuccessStatus(@RequestBody Cash cash) {
        return bankAccountService.decreaseSumOnBankAccount(cash);
    }
}
