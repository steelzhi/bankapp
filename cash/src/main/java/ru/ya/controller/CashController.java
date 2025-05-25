package ru.ya.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ya.enums.ErrorOperation;
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
        return responseFromModule.getResponseForSuccessfullOpFromModuleNotifications("/notification/success", new Operation(SuccessfullOperation.SUM_INCREASING));
    }

    @PostMapping("/decrease-sum")
    public String decreaseSumOnBankAccount(@RequestBody Cash cash) {
        logger.atInfo().log("Decreasing sum on user with login = " + cash.getUserLogin());
        boolean response = responseFromModule.getResponseForDecreaseOpFromModuleAccounts("/decrease-sum", cash);
        if (response) {
            return responseFromModule.getResponseForSuccessfullOpFromModuleNotifications("/notification/success", new Operation(SuccessfullOperation.SUM_DECREASING));
        } else {
            return responseFromModule.getResponseForSuccessfullOpFromModuleNotifications("/notification/error", new Operation(ErrorOperation.NOT_ENOUGH_MONEY));
        }
    }

}
