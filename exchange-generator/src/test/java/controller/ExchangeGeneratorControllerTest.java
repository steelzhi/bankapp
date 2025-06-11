/*
package controller;

import config.ExchangeGeneratorConfigTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.ya.controller.ExchangeGeneratorController;
import ru.ya.model.CurrencyRates;
import ru.ya.service.ExchangeGeneratorService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
@Import(ExchangeGeneratorConfigTest.class)
@SpringBootTest(classes = ExchangeGeneratorController.class)
@ContextConfiguration(classes = ExchangeGeneratorController.class)
public class ExchangeGeneratorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    ExchangeGeneratorService exchangeGeneratorService;

    @Test
    void getCurrencyRates() {
            when(exchangeGeneratorService.getCurrencyRates())
                    .thenReturn(new CurrencyRates());

            CurrencyRates currencyRates = exchangeGeneratorService.getCurrencyRates();
            assertNotNull(currencyRates, "Currency rates should be not null");

            verify(exchangeGeneratorService, times(1)).getCurrencyRates();
    }
}
*/
