package ru.ya.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import ru.ya.dto.BankAccountDto;
import ru.ya.dto.UserDto;
import ru.ya.enums.Currency;
import ru.ya.enums.ErrorOperation;
import ru.ya.enums.SuccessfullOperation;
import ru.ya.model.BankAccount;
import ru.ya.model.NewAccountCurrency;
import ru.ya.model.Operation;
import ru.ya.model.User;
import ru.ya.repository.BankAccountRepository;
import ru.ya.repository.UserRepository;
import ru.ya.service.BankAccountService;
import ru.ya.service.UserService;
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
}
