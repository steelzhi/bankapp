package ru.ya.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.ya.dto.CoupleOfValuesDto;
import ru.ya.dto.TransferDataDto;
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

    public void deleteBankAccount(int id) {
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
        double sumOnBankAccount = bankAccountRepository.getAccountValueByIdAndAccountNumber(transferData.getSenderId(), transferData.getAccountNumberFrom());
        if (sumOnBankAccount >= transferData.getSum()) {
            Currency currencyFrom = Currency.valueOf(bankAccountRepository.getCurrencyByAccountNumber(transferData.getAccountNumberFrom()));
            String currencyNameFrom = currencyFrom.name();
            String currencyNameTo = transferData.getCurrencyNameTo();
            if (transferData.getAccountNumberTo() != null) {
                Currency currencyTo = Currency.valueOf(bankAccountRepository.getCurrencyByAccountNumber(transferData.getAccountNumberTo()));
                currencyNameTo = currencyTo.name();
            }

            TransferDataDto transferDataDto = TransferDataMapper.mapToTransferDataDto(transferData, currencyNameFrom, currencyNameTo);
            return transferDataDto;
        }

        return null;
    }

    public boolean doesReceiverHaveBankAccount(@RequestBody TransferData transferData) {
        String accountNumberTo = bankAccountRepository.findBankAccountByIdAndCurrencyName(transferData.getReceiverId(), Currency.valueOf(transferData.getCurrencyNameTo()));
        return accountNumberTo != null;
    }

    @Transactional
    public void transfer(CoupleOfValuesDto coupleOfValuesDto) {
/*        if (coupleOfValuesDto.getReceiverId() == 0) { // Если id = 0, значит, производится перевод между счетами одного клиента

        }*/
        bankAccountRepository.transferFrom(coupleOfValuesDto.getAccountNumberFrom(), coupleOfValuesDto.getValueFrom());

        if (coupleOfValuesDto.getReceiverId() == 0) { // Если клиент переводит между своими счетами
            bankAccountRepository.transferToByAccountNumber(coupleOfValuesDto.getAccountNumberTo(), coupleOfValuesDto.getValueTo());
        } else {
            bankAccountRepository.transferToByCurrencyValue(coupleOfValuesDto.getReceiverId(), Currency.valueOf(coupleOfValuesDto.getCurrencyNameTo()), coupleOfValuesDto.getValueTo());
        }
    }

    public BankAccount setAccountNumber(BankAccount bankAccount) {
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
