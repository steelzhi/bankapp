package ru.ya.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ya.dto.CoupleOfValuesDto;
import ru.ya.dto.TransferDataDto;
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
            return responseFromModule.getResponseFromModuleNotifications("/notification/error", new Operation(ErrorOperation.BANK_ACCOUNT_ALREADY_EXISTS));
        }

        //return getResponseFromModuleNotifications("/notification/success", new Operation(SuccessfullOperation.BANK_ACCOUNT_CREATING));
        return responseFromModule.getResponseFromModuleNotifications("/notification/success", new Operation(SuccessfullOperation.BANK_ACCOUNT_CREATING));
    }

    @PostMapping("/delete-bank-account")
    public String deleteBankAccount(@RequestBody int id) {
        logger.atInfo().log("Deleting bank account with id = " + id);
        boolean isBankAccountEmpty = bankAccountService.isBankAccountEmpty(id);
        if (!isBankAccountEmpty) {
            return responseFromModule.getResponseFromModuleNotifications("/notification/error", new Operation(ErrorOperation.BANK_ACCOUNT_IS_NOT_EMPTY));
        }

        bankAccountService.deleteBankAccount(id);
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

    @PostMapping("/is-possible-to-transfer")
    public TransferDataDto getTransferDataDtoIfUserHasEnoughMoneyToTransfer(@RequestBody TransferData transferData) {
        TransferDataDto transferDataDto = bankAccountService.getTransferDataDtoIfUserHasEnoughMoneyToTransfer(transferData);
        return transferDataDto;
    }

    @PostMapping("/does-receiver-have-bank-account")
    public Boolean doesReceiverHaveBankAccount(@RequestBody TransferData transferData) {
        return bankAccountService.doesReceiverHaveBankAccount(transferData);
    }

    @PostMapping("/transfer")
    public String transfer(@RequestBody CoupleOfValuesDto coupleOfValuesDto) {
        bankAccountService.transfer(coupleOfValuesDto);
        return responseFromModule.getResponseFromModuleNotifications("/notification/success", new Operation(SuccessfullOperation.TRANSFER_SUCCESSFULL));
    }
}
