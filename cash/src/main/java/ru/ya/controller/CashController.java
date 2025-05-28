package ru.ya.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ya.enums.ErrorOperation;
import ru.ya.enums.SuccessfullOperation;
import ru.ya.model.Cash;
import ru.ya.model.Operation;
import ru.ya.util.ResponseFromModule;

@RestController
public class CashController {

    @Autowired
    Logger logger;

    @Autowired
    ResponseFromModule responseFromModule;

    @PostMapping("/increase-sum")
    public String increaseSumOnBankAccount(@RequestBody Cash cash) {
        logger.atInfo().log("Increasing sum on user with login = " + cash.getUserLogin());
        responseFromModule.getStringResponseFromModuleAccounts("/increase-sum", cash);
        return responseFromModule.getStringResponseForSuccessfullOpFromModuleNotifications("/notification/success", new Operation(SuccessfullOperation.SUM_INCREASING));
    }

    @PostMapping("/decrease-sum")
    public String decreaseSumOnBankAccount(@RequestBody Cash cash) {
        logger.atInfo().log("Decreasing sum on user with login = " + cash.getUserLogin());
        boolean isOpSuspicious = responseFromModule.getBooleanResponseForSuspiciousOpsFromModuleBlocker("/check-operation");
        if (isOpSuspicious) {
            return "operation-decrease-is-suspicious.html";
        }
        boolean wasSumDecreased = responseFromModule.getBooleanResponseForDecreaseOpFromModuleAccounts("/decrease-sum", cash);
        if (wasSumDecreased) {
            return responseFromModule.getStringResponseForSuccessfullOpFromModuleNotifications("/notification/success", new Operation(SuccessfullOperation.SUM_DECREASING));
        } else {
            return responseFromModule.getStringResponseForSuccessfullOpFromModuleNotifications("/notification/error", new Operation(ErrorOperation.NOT_ENOUGH_MONEY));
        }
    }

}
