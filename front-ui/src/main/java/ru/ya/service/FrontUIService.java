package ru.ya.service;

import org.springframework.stereotype.Service;
import ru.ya.model.BankAccount;
import ru.ya.model.User;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class FrontUIService {
    public static final int USER_NAME_MIN_LENGTH = 1;
    public static final int USER_NAME_MAX_LENGTH = 20;
    public static final int USER_SURNAME_MIN_LENGTH = 1;
    public static final int USER_SURNAME_MAX_LENGTH = 20;

    public boolean isUserPasswordCorrect(User user) {
        return (user.getPassword().equals(user.getConfirmedPassword()));
    }

    public boolean isOtherUserDataCorrect(User user) {
        return (user.getName().length() >= USER_NAME_MIN_LENGTH
                && user.getName().length() <= USER_NAME_MAX_LENGTH
                && user.getName().length() >= USER_SURNAME_MIN_LENGTH
                && user.getName().length() <= USER_SURNAME_MAX_LENGTH
                && isUserAnAdult(user));
    }

    public boolean isUserAnAdult(User user) {
        long numberOfYears = ChronoUnit.YEARS.between(user.getBirthdate(), LocalDate.now());
        return (numberOfYears >= 18);
    }

    public boolean areAllUsersBankAccountsEmpty(User user) {
        List<BankAccount> bankAccountList = user.getBankAccountList();
        for (BankAccount bankAccount : bankAccountList) {
            if (bankAccount.getAccountValue() > 0) {
                return false;
            }
        }

        return true;
    }


}
