package ru.ya.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ya.enums.SuccessfullOperation;
import ru.ya.model.Cash;
import ru.ya.model.Operation;
import ru.ya.service.CashService;
import ru.ya.util.ResponseFromModule;

@RestController
public class CashController {
    @Autowired
    CashService cashService;

    @Autowired
    Logger logger;

    @Autowired
    ResponseFromModule responseFromModule;

    @PostMapping("/increase-sum")
    public String increaseSumOnBankAccount(@RequestBody Cash cash) {
        logger.atInfo().log("Increasing sum on user with login = " + cash.getUserLogin());
        responseFromModule.getResponseFromModuleAccounts("/increase-sum", cash);
        String s = responseFromModule.getResponseForSuccessfullOpFromModuleNotifications("/notification/success", new Operation(SuccessfullOperation.SUM_INCREASING));
        return s;
    }

/*    @PostMapping("/delete-bank-account")
    public String deleteBankAccount(@RequestBody int id) {
        logger.atInfo().log("Deleting bank account with id = " + id);
        cashService.deleteBankAccountAndReturnIfDeleted(id);
        return responseFromModule.getResponseFromModuleNotifications("/notification/success", new Operation(SuccessfullOperation.BANK_ACCOUNT_DELETING));
    }*/
}
