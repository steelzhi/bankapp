package service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.ya.model.CurrencyRates;
import ru.ya.service.ExchangeGeneratorService;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = ExchangeGeneratorService.class)
public class ExchangeGeneratorServiceTest {


    @Autowired
    ExchangeGeneratorService exchangeGeneratorService;

    @Test
    void getCurrencyRates() {
        CurrencyRates currencyRates = exchangeGeneratorService.getCurrencyRates();

        assertNotNull(currencyRates, "Currency rates should be not null");
    }

}
