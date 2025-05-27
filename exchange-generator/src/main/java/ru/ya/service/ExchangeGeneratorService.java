package ru.ya.service;

import org.springframework.stereotype.Service;
import ru.ya.enums.Currency;
/*import ru.ya.model.CurrencyRate;*/
import ru.ya.model.CurrencyRates;

@Service
public class ExchangeGeneratorService {
    CurrencyRates currencyRatesList;

    public CurrencyRates getCurrencyRates() {
        currencyRatesList = new CurrencyRates();
        CurrencyRates.CurrencyRate currencyRatesUsd = new CurrencyRates.CurrencyRate(85.00, (double) Math.round(Math.random() * 0.07 * 100) / 100, Currency.USD.name());
        CurrencyRates.CurrencyRate currencyRatesCny = new CurrencyRates.CurrencyRate(13.00, (double) Math.round(Math.random() * 0.055 * 100) / 100, Currency.CNY.name());
        currencyRatesList.getCurrencyRatesList().add(currencyRatesUsd);
        currencyRatesList.getCurrencyRatesList().add(currencyRatesCny);
        return currencyRatesList;
    }
}
