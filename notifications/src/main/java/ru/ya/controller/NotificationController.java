package ru.ya.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ya.model.Operation;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    Logger logger;

    @PostMapping("/success")
    public String getNotificationAboutSuccessfullOp(@RequestBody Operation operation) {
        switch (operation.getSuccessfullOperation()) {
            case USER_CREATING -> {
                return "user-registered-successfully.html";
            }
            case PASSWORD_EDITING -> {
                return "password-changed-successfully.html";
            }
            case OTHER_DATA_EDITING -> {
                return "user-data-changed-successfully.html";
            }
            case BANK_ACCOUNT_CREATING -> {
                return "bank-account-added-successfully.html";
            }
            case BANK_ACCOUNT_DELETING -> {
                return "bank-account-deleted-successfully.html";
            }
            case SUM_INCREASING -> {
                return "sum-on-bank-account-increased-successfully.html";
            }
            case SUM_DECREASING -> {
                return "sum-on-bank-account-decreased-successfully.html";
            }
            case TRANSFER_SUCCESSFULL -> {
                return "transaction-successfull.html";
            }
        }

        return "redirect:/";
    }

    @PostMapping("/error")
    public String getNotificationAboutError(Model model, @RequestBody Operation operation) {
        //model.addAttribute("operation", operation);
        switch (operation.getErrorOperation()) {
            case USER_ALREADY_EXISTS -> {
                return "user-already-exists.html";
            }
            case BANK_ACCOUNT_ALREADY_EXISTS -> {
                return "bank-account-already-exists.html";
            }
            case BANK_ACCOUNT_IS_NOT_EMPTY -> {
                return "bank-account-is-not-empty.html";
            }
            case NOT_ENOUGH_MONEY -> {
                return "sum-on-bank-account-was-not-decreased.html";
            }
            case NOT_ENOUGH_MONEY_TO_TRANSFER -> {
                return "not-enough-money-to-transfer.html";
            }
            case SUSPICIOUS_OPERATION -> {
                return "operation-transfer-is-suspicious.html";
            }
            case CLIENT_DOES_NOT_HAVE_BANK_ACCOUNT -> {
                return "receiver-does-not-have-bank-account.html";
            }
        }

        return "redirect:/";
    }
}
