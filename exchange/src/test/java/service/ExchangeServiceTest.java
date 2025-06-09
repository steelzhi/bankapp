package service;

import config.ExchangeConfigurationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.ya.dto.TransferDataDto;
import ru.ya.model.CoupleOfValues;
import ru.ya.model.CurrencyRates;
import ru.ya.service.ExchangeService;
import ru.ya.util.ResponseFromModule;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ExchangeService.class})
@Import(ExchangeConfigurationTest.class)
public class ExchangeServiceTest {

    @Autowired
    ExchangeService exchangeService;

    @MockitoBean
    ResponseFromModule responseFromModule;

    @Test
    void getConvertedSum() {
        CurrencyRates currencyRates = new CurrencyRates();
        currencyRates.getCurrencyRatesList().add(
                new CurrencyRates.CurrencyRate(200, 2.7, "USD"));
        currencyRates.getCurrencyRatesList().add(
                new CurrencyRates.CurrencyRate(100, 0.7, "RUB"));
        when(responseFromModule.getCurrencyRatesResponseFromModuleExchangeGenerator(exchangeService.moduleExchangeGenerator, "/exchange-rates"))
                .thenReturn(currencyRates);

        CoupleOfValues coupleOfValues = exchangeService.getConvertedSum(new TransferDataDto(1, 2, "11", "RUB", "22", "USD", 20));

        assertNotNull(coupleOfValues, "Couple of values should not be null");
        assertTrue(coupleOfValues.getValueFrom() > 0 && coupleOfValues.getValueTo() > 0, "Values should be positive");
    }
}
