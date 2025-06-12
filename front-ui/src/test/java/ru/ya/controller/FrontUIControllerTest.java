package ru.ya.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.ya.config.ConfigTest;
import ru.ya.model.CurrencyRates;
import ru.ya.model.TransferData;
import ru.ya.model.User;
import ru.ya.service.FrontUIService;
import ru.ya.util.ResponseFromModule;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(FrontUIController.class)
@AutoConfigureMockMvc
@Import({ConfigTest.class})
public class FrontUIControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FrontUIService frontUIService;

    @MockitoBean
    private ResponseFromModule responseFromModule;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAccountData() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name( "main-page"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getRegisterForm() throws Exception {
        mockMvc.perform(get("/get-register-form"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name( "register-form"));
    }

    @Test
    @WithMockUser(username = "1", roles = {"USER"}, password = "2")
    void registerUserWithIncorrectPassword() throws Exception {
        when(frontUIService.isUserPasswordCorrect(new User()))
                .thenReturn(false);

        mockMvc.perform(post("/register-user"))
                .andExpect(status().is2xxSuccessful());

        verify(frontUIService, times(1)).isUserPasswordCorrect(new User());
    }

    @Test
    @WithMockUser(username = "1", roles = {"USER"}, password = "2")
    void registerUserWithCorrectPassword() throws Exception {
        when(frontUIService.isUserPasswordCorrect(new User()))
                .thenReturn(true);

        mockMvc.perform(post("/register-user"))
                .andExpect(status().is2xxSuccessful());

        verify(frontUIService, times(1)).isUserPasswordCorrect(new User());
    }

/*    @Test
    @WithMockUser(username = "1", roles = {"USER"}, password = "2")
    void editPasswordCorrectly() throws Exception {
        when(frontUIService.isUserPasswordCorrect(new User("1", null, null, null, null, null)))
                .thenReturn(true);

        mockMvc.perform(post("/user/1/edit-password"))
                .andExpect(status().is2xxSuccessful());

        verify(frontUIService, times(1)).isUserPasswordCorrect(new User());
    }


    @Test
    @WithMockUser(username = "1", roles = {"USER"}, password = "2")
    void editOtherData() throws Exception {
        when(frontUIService.isOtherUserDataCorrect(new User("1", null, null, null, null, null)))
                .thenReturn(true);

        mockMvc.perform(post("/user/1/edit-other-data"))
                .andExpect(status().is2xxSuccessful());

        verify(frontUIService, times(1)).isOtherUserDataCorrect(new User());
    }*/


    @Test
    @WithMockUser(username = "1", roles = {"USER"}, password = "2")
    void increaseSumOnBankAccount() throws Exception {
        mockMvc.perform(post("/user/increase-sum"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "1", roles = {"USER"}, password = "2")
    void decreaseSumOnBankAccount() throws Exception {
        mockMvc.perform(post("/user/decrease-sum"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "1", roles = {"USER"}, password = "2")
    void getCurrencyRates() throws Exception {
        when(responseFromModule.getCurrencyRatesResponseFromModuleExchangeGenerator(anyString(), anyString()))
                .thenReturn(new CurrencyRates());

        mockMvc.perform(get("/exchange-rates"))
                .andExpect(status().is2xxSuccessful());

        verify(responseFromModule, times(1)).getCurrencyRatesResponseFromModuleExchangeGenerator(anyString(), anyString());
    }

    @Test
    @WithMockUser(username = "1", roles = {"USER"}, password = "1")
    void transferToSameAccount() throws Exception {
        TransferData transferData = new TransferData();
        when(frontUIService.doUsersBankAccountsMatch(transferData))
                .thenReturn(true);

        mockMvc.perform(post("/transfer"))
                .andExpect(status().is3xxRedirection());

        verify(frontUIService, times(1)).doUsersBankAccountsMatch(transferData);
    }

    @Test
    @WithMockUser(username = "1", roles = {"USER"}, password = "1")
    void transferToAnotherAccount() throws Exception {
        TransferData transferData = new TransferData();
        when(frontUIService.doUsersBankAccountsMatch(transferData))
                .thenReturn(false);

        when(responseFromModule.getStringResponseFromModuleTransfer("/transfer", transferData))
                .thenReturn("main-page");

        mockMvc.perform(post("/transfer"))
                .andExpect(status().is2xxSuccessful());

        verify(frontUIService, times(1)).doUsersBankAccountsMatch(any());
        verify(responseFromModule, times(1)).getStringResponseFromModuleTransfer("/transfer", transferData);

    }

}
