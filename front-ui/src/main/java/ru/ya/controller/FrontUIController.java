package ru.ya.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ya.dto.TransferDataDto;
import ru.ya.dto.UserDto;
import ru.ya.mapper.TransferDataMapper;
import ru.ya.mapper.UserMapper;
import ru.ya.model.*;
import ru.ya.service.FrontUIService;
import ru.ya.util.ResponseFromModule;

@Controller
public class FrontUIController {
    @Value("${module-accounts}")
    private String moduleAccountsHost;

    @Value("${module-exchange-generator}")
    private String moduleExchangeGenerator;

    @Value("${module-transfer}")
    private String moduleTransfer;

    @Autowired
    private FrontUIService frontUIService;

    @Autowired
    ResponseFromModule responseFromModule;

    @Autowired
    UserDetailsService userDetailsService;

    @GetMapping("/")
    public String getMainPage() {
        return "main-page";
    }

    @GetMapping("/get-register-form")
    public String getRegisterForm() {
        return "register-form";
    }

    @PostMapping("/register-user")
    public String registerUser(Model model, @ModelAttribute User user) {
        model.addAttribute("login", user.getLogin());
        if (!frontUIService.isUserPasswordCorrect(user)) {
            return "user-password-was-not-confirmed.html";
        }
        if (!frontUIService.isUserAnAdult(user)) {
            return "user-is-not-an-adult.html";
        }

        UserDto userDto = UserMapper.mapToUserDto(user);
        return responseFromModule.getStringResponseFromModuleAccounts("/register-user", userDto);
    }

    @GetMapping("/account")
    public String enterToAccount(Model model) {
        String userDtoLogin = getUserDtoInSystem().getLogin();
        UserDto userDto = responseFromModule.getUserDtoResponseFromModuleAccounts(userDtoLogin);
        model.addAttribute("userDto", userDto);

        CurrencyRates currencyRates = getCurrencyRates();
        model.addAttribute("currencyRates", currencyRates);

        return "account";
    }

    @PostMapping("/user/{login}/edit-password")
    public String editPassword(Model model, @ModelAttribute User user) {
        model.addAttribute("login", user.getLogin());
        if (!frontUIService.isUserPasswordCorrect(user)) {
            return "user-password-was-not-confirmed.html";
        }

        UserDto userDto = UserMapper.mapToUserDto(user);
        return responseFromModule.getStringResponseFromModuleAccounts("/edit-password", userDto);
    }

    @PostMapping("/user/{login}/edit-other-data")
    public String editOtherData(Model model, @ModelAttribute User user) {
        model.addAttribute("login", user.getLogin());
        if (!frontUIService.isOtherUserDataCorrect(user)) {
            return "user-data-is-incorrect.html";
        }

        UserDto userDto = UserMapper.mapToUserDto(user);
        return responseFromModule.getStringResponseFromModuleAccounts("/edit-other-data", userDto);
    }

    @PostMapping(value = "/user/{login}/delete-user", params = "_method=delete")
    public String deleteUser(Model model) {
        UserDto userDto = getUserDtoInSystem();
        model.addAttribute("login", userDto.getLogin());
        if (!frontUIService.areAllUsersBankAccountsEmpty(userDto)) {
            return "user-bank-accounts-are-not-empty.html";
        }

        responseFromModule.getStringResponseFromModuleAccounts("/delete-user", userDto);

        return "redirect:/logout";
    }

    @PostMapping("/user/add-bank-account")
    public String addBankAccount(Model model, @ModelAttribute NewAccountCurrency newAccountCurrency) {
        UserDto userDto = getUserDtoInSystem();
        newAccountCurrency.setUserDto(userDto);

        model.addAttribute("login", userDto.getLogin());
        model.addAttribute("currency", newAccountCurrency.getCurrency());
        return responseFromModule.getStringResponseFromModuleAccounts("/add-bank-account", newAccountCurrency);
    }

    @PostMapping(value = "/user/delete-bank-account/{id}", params = "_method=delete")
    public String deleteBankAccount(Model model, @PathVariable(name = "id") int id) {
        UserDto userDto = getUserDtoInSystem();
        model.addAttribute("login", userDto.getLogin());

        return responseFromModule.getStringResponseFromModuleAccounts("/delete-bank-account", id);
    }

    @PostMapping("/user/increase-sum")
    public String increaseSumOnBankAccount(Model model, @ModelAttribute Cash cash) {
        if (cash.getSum() == null || cash.getSum() <= 0) {
            return "redirect:/account";
        }

        model.addAttribute("cash", cash);
        return responseFromModule.getStringResponseFromModuleCash("/increase-sum", cash);
    }

    @PostMapping("/user/decrease-sum")
    public String decreaseSumOnBankAccount(Model model, @ModelAttribute Cash cash) {
        if (cash.getSum() == null || cash.getSum() <= 0) {
            return "redirect:/account";
        }

        model.addAttribute("cash", cash);
        return responseFromModule.getStringResponseFromModuleCash("/decrease-sum", cash);
    }

    @GetMapping("/exchange-rates")
    @ResponseBody
    public CurrencyRates getCurrencyRates() {
        CurrencyRates currencyRates = responseFromModule.getCurrencyRatesResponseFromModuleExchangeGenerator(moduleExchangeGenerator, "/exchange-rates");
        return currencyRates;
    }

    @PostMapping("/transfer")
    public String transfer(Model model, @ModelAttribute TransferData transferData) {
        if (frontUIService.doBankAccountsMatch(transferData)) {
            return "redirect:/account";
        }

        model.addAttribute(transferData.getUserId());
        return responseFromModule.getStringResponseFromModuleTransfer("/transfer", transferData);
    }

    private UserDto getUserDtoInSystem() {
        //String login = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
/*        if (authentication != null) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            login = userDetails.getUsername();
        }*/

        // При таком приведении у UserDto теряются значения из BankAccountDtoList
/*        UserDetails userDetails = userDetailsService.loadUserByUsername(login);
        UserPrincipal userPrincipal = (UserPrincipal) userDetails;*/

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserDto userDto = userPrincipal.getUserDto();

        return userDto;
    }
}
