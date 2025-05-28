package ru.ya.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.ya.dto.TransferDataDto;
import ru.ya.dto.UserDto;
import ru.ya.enums.Currency;
import ru.ya.mapper.TransferDataMapper;
import ru.ya.model.*;
import ru.ya.repository.BankAccountRepository;
import ru.ya.repository.UserRepository;

@Service
public class BankAccountService {
    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    UserRepository userRepository;

    public BankAccount addBankAccount(NewAccountCurrency newAccountCurrency) {
        User user = userRepository.findByLogin(newAccountCurrency.getUserDto().getLogin());
        // Проверяем, есть ли уже у пользователя счет в такой валюте
        for (BankAccount bankAccount : user.getBankAccountList()) {
            if (bankAccount.getCurrency() == newAccountCurrency.getCurrency()) {
                return null;
            }
        }

        BankAccount bankAccount = new BankAccount(user, 0, newAccountCurrency.getCurrency());
        setAccountNumber(bankAccount);
        BankAccount addedBankAccount = bankAccountRepository.save(bankAccount);
        return addedBankAccount;
    }

    public void deleteBankAccountAndReturnIfDeleted(int id) {
        bankAccountRepository.deleteById(id);
    }

    public boolean isBankAccountEmpty(int id) {
        return (bankAccountRepository.findAccountValueById(id) == 0);
    }

    public void increaseSumOnBankAccount(Cash cash) {
        bankAccountRepository.increaseSumOnBankAccount(cash.getSum(), cash.getAccountNumber());
    }

    public boolean decreaseSumOnBankAccount(Cash cash) {
        BankAccount bankAccount = bankAccountRepository.findByAccountNumber(cash.getAccountNumber());
        if (bankAccount.getAccountValue() < cash.getSum()) {
            //throw new NotEnoughMoneyException("Недостаточно средств на счете");
            return false;
        }

        bankAccountRepository.decreaseSumOnBankAccount(cash.getSum(), cash.getAccountNumber());
        return true;
    }

    public TransferDataDto getTransferDataDtoIfUserHasEnoughMoneyToTransfer(TransferData transferData) {
        double sumOnBankAccount = bankAccountRepository.getAccountValueByIdAndAccountNumber(transferData.getUserId(), transferData.getAccountNumberFrom());
        if (sumOnBankAccount >= transferData.getSum()) {
            Currency currencyFrom = Currency.valueOf(bankAccountRepository.getCurrencyByAccountNumber(transferData.getAccountNumberFrom()));
            String currencyNameFrom = currencyFrom.name();
            Currency currencyTo = Currency.valueOf(bankAccountRepository.getCurrencyByAccountNumber(transferData.getAccountNumberTo()));
            String currencyNameTo = currencyTo.name();
            TransferDataDto transferDataDto = TransferDataMapper.mapToTransferDataDto(transferData, currencyNameFrom, currencyNameTo);
            return transferDataDto;
        }

        return null;
    }

    private BankAccount setAccountNumber(BankAccount bankAccount) {
        switch (bankAccount.getCurrency()) {
            case Currency.RUB -> bankAccount.setAccountNumber("4071" + getRandomTail());
            case Currency.USD -> bankAccount.setAccountNumber("3651" + getRandomTail());
            case Currency.CNY -> bankAccount.setAccountNumber("7410" + getRandomTail());
        }
        return bankAccount;
    }

    private String getRandomTail() {
        return String.valueOf(Math.round(Math.random() * 1_000_000));
    }

}
