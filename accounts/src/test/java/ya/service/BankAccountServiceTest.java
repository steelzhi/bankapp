package ya.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.ya.dto.CoupleOfValuesDto;
import ru.ya.dto.UserDto;
import ru.ya.enums.Currency;
import ru.ya.enums.Roles;
import ru.ya.mapper.UserMapper;
import ru.ya.model.*;
import ru.ya.repository.BankAccountRepository;
import ru.ya.repository.UserRepository;
import ru.ya.service.BankAccountService;
import ru.ya.service.UserService;
import ya.config.AccountsConfigurationTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest(classes = {BankAccountService.class, BankAccountRepository.class})
@Import(AccountsConfigurationTest.class)
public class BankAccountServiceTest {

    @Autowired
    BankAccountService bankAccountService;

    @MockitoBean
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @MockitoBean
    BankAccountRepository bankAccountRepository;

    @Test
    void addBankAccount() {
        User user = new User("1", "1", "1", "1", LocalDate.now(), Roles.USER, new ArrayList<>());
        UserDto userDto = UserMapper.mapToUserDto(user);
        NewAccountCurrency newAccountCurrency = new NewAccountCurrency(Currency.USD, userDto);

        when(userRepository.findByLogin(newAccountCurrency.getUserDto().getLogin()))
                .thenReturn(user);

        BankAccount bankAccount = new BankAccount(user, 0, newAccountCurrency.getCurrency());
        bankAccountService.setAccountNumber(bankAccount);


        when(bankAccountRepository.save(bankAccount))
                .thenReturn(bankAccount);

        bankAccountService.addBankAccount(newAccountCurrency);

        verify(userRepository, times(1)).findByLogin(newAccountCurrency.getUserDto().getLogin());
    }

    @Test
    void deleteBankAccount() {
        doNothing().when(bankAccountRepository).deleteById(1);
        bankAccountService.deleteBankAccount(1);

        verify(bankAccountRepository, times(1)).deleteById(1);
    }

    @Test
    void isBankAccountEmpty() {
        when(bankAccountRepository.findAccountValueById(1))
                .thenReturn(2.0);

        bankAccountService.isBankAccountEmpty(1);

        verify(bankAccountRepository, times(1)).findAccountValueById(1);
    }

    @Test
    void increaseSumOnBankAccount() {
        Cash cash = new Cash();
        doNothing().when(bankAccountRepository).increaseSumOnBankAccount(cash.getSum(), cash.getAccountNumber());

        bankAccountService.increaseSumOnBankAccount(cash);

        verify(bankAccountRepository, times(1)).increaseSumOnBankAccount(cash.getSum(), cash.getAccountNumber());
    }

    @Test
    void decreaseSumOnBankAccount() {
        User user = new User("1", "1", "1", "1", LocalDate.now(), Roles.USER, new ArrayList<>());
        UserDto userDto = UserMapper.mapToUserDto(user);
        NewAccountCurrency newAccountCurrency = new NewAccountCurrency(Currency.USD, userDto);
        BankAccount bankAccount = new BankAccount(user, 0, newAccountCurrency.getCurrency());
        Cash cash = new Cash();

        when(bankAccountRepository.findByAccountNumber(cash.getAccountNumber()))
                .thenReturn(bankAccount);

        doNothing().when(bankAccountRepository).decreaseSumOnBankAccount(cash.getSum(), cash.getAccountNumber());
        bankAccountService.decreaseSumOnBankAccount(cash);

        verify(bankAccountRepository, times(1)).decreaseSumOnBankAccount(cash.getSum(), cash.getAccountNumber());
    }

    @Test
    void getTransferDataDtoIfUserHasEnoughMoneyToTransfer() {
        TransferData transferData = new TransferData();
        when(bankAccountRepository.getAccountValueByIdAndAccountNumber(transferData.getSenderId(), transferData.getAccountNumberFrom()))
                .thenReturn(-2.0);

        bankAccountService.getTransferDataDtoIfUserHasEnoughMoneyToTransfer(transferData);

        verify(bankAccountRepository, times(1)).getAccountValueByIdAndAccountNumber(transferData.getSenderId(), transferData.getAccountNumberFrom());
    }

    @Test
    void doesReceiverHaveBankAccount() {
        TransferData transferData = new TransferData(1, 2, "11", "22", "RUB", 2.0);
        when(bankAccountRepository.findBankAccountByIdAndCurrencyName(transferData.getReceiverId(), Currency.valueOf(transferData.getCurrencyNameTo())))
                .thenReturn("111");

        bankAccountService.doesReceiverHaveBankAccount(transferData);

        verify(bankAccountRepository, times(1)).findBankAccountByIdAndCurrencyName(transferData.getReceiverId(), Currency.valueOf(transferData.getCurrencyNameTo()));
    }

    @Test
    void transfer() {
        CoupleOfValuesDto coupleOfValuesDto = new CoupleOfValuesDto(1, "1", "RUB", 2.0, 2, "22", "USD", 0.03);

        doNothing().when(bankAccountRepository).transferFrom(coupleOfValuesDto.getAccountNumberFrom(), coupleOfValuesDto.getValueFrom());
        doNothing().when(bankAccountRepository).transferToByCurrencyValue(coupleOfValuesDto.getReceiverId(), Currency.valueOf(coupleOfValuesDto.getCurrencyNameTo()), coupleOfValuesDto.getValueTo());

        bankAccountService.transfer(coupleOfValuesDto);
        verify(bankAccountRepository, times(1)).transferFrom(coupleOfValuesDto.getAccountNumberFrom(), coupleOfValuesDto.getValueFrom());
       }

}
