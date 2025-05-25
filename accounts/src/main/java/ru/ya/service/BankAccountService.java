package ru.ya.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.ya.enums.Currency;
import ru.ya.model.BankAccount;
import ru.ya.model.Cash;
import ru.ya.model.NewAccountCurrency;
import ru.ya.model.User;
import ru.ya.repository.BankAccountRepository;
import ru.ya.repository.UserRepository;

@Service
public class BankAccountService {
    //private static final BigInteger TAIL = new BigInteger("100000000000000");

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

    public void increaseSumOnBankAccount(Cash cash) {
        bankAccountRepository.increaseSumOnBankAccount(cash.getSum(), cash.getAccountNumber());
    }

    private BankAccount setAccountNumber(BankAccount bankAccount) {
        switch (bankAccount.getCurrency()) {
            case Currency.RUB -> bankAccount.setAccountNumber("4071" + getRandomTail() + " (рубли)");
            case Currency.USD -> bankAccount.setAccountNumber("3651" + getRandomTail() + " (доллары)");
            case Currency.CNY -> bankAccount.setAccountNumber("7410" + getRandomTail() + " (юани)");
        }
        return bankAccount;
    }

    private String getRandomTail() {
        return String.valueOf(Math.round(Math.random() * 1_000_000));
    }

}
