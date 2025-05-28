package ru.ya.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.ya.enums.Currency;
/*import ru.ya.model.CurrencyRate;*/
import ru.ya.model.CurrencyRates;

@Service
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExchangeGeneratorService {
    final double CURRENCY_VALUE_FOR_USD = 85.00;
    final double CURRENCY_VALUE_FOR_CNY = 13.00;
    final double TOLERANCE_COEF_FOR_USD = 0.07;
    final double TOLERANCE_COEF_FOR_CNY = 0.055;

    CurrencyRates currencyRatesList;

    public CurrencyRates getCurrencyRates() {
        currencyRatesList = new CurrencyRates();
        CurrencyRates.CurrencyRate currencyRatesUsd = new CurrencyRates.CurrencyRate(CURRENCY_VALUE_FOR_USD, (double) Math.round(Math.random() * TOLERANCE_COEF_FOR_USD * 100) / 100, Currency.USD.name());
        CurrencyRates.CurrencyRate currencyRatesCny = new CurrencyRates.CurrencyRate(CURRENCY_VALUE_FOR_CNY, (double) Math.round(Math.random() * TOLERANCE_COEF_FOR_CNY * 100) / 100, Currency.CNY.name());
        currencyRatesList.getCurrencyRatesList().add(currencyRatesUsd);
        currencyRatesList.getCurrencyRatesList().add(currencyRatesCny);
        return currencyRatesList;
    }
}
