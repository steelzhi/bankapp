package ru.ya.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.ya.dto.TransferDataDto;
import ru.ya.enums.Currency;
import ru.ya.model.CoupleOfValues;
import ru.ya.model.CurrencyRates;
import ru.ya.util.ResponseFromModule;

@Service
public class ExchangeService {

    @Value("${module-exchange-generator}")
    public String moduleExchangeGenerator;

    @Autowired
    ResponseFromModule responseFromModule;

    public CoupleOfValues getConvertedSum(TransferDataDto transferDataDto) {
            CurrencyRates currencyRates = responseFromModule.getCurrencyRatesResponseFromModuleExchangeGenerator(moduleExchangeGenerator, "/exchange-rates");
        double sum = transferDataDto.getSum();
        double deltaFrom = 0;
        double deltaTo = 0;

        // Вычитаем со счета сумму, которую будем списывать
        deltaFrom += sum;

        // Если валюта счета списания не ₽, нужно перевести ее в ₽
        if (!transferDataDto.getCurrencyNameFrom().equals(Currency.RUB.name())) {
            for (CurrencyRates.CurrencyRate currencyRate : currencyRates.getCurrencyRatesList()) {
                if (transferDataDto.getCurrencyNameFrom().equals(currencyRate.getCurrencyName())) {
                    sum *= currencyRate.getSellRate();
                    break;
                }
            }
        }

        // Если валюта счета зачисления не ₽, нужно перевести ее в ₽
        if (!transferDataDto.getCurrencyNameTo().equals(Currency.RUB.name())) {
            for (CurrencyRates.CurrencyRate currencyRate : currencyRates.getCurrencyRatesList()) {
                if (transferDataDto.getCurrencyNameTo().equals(currencyRate.getCurrencyName())) {
                    sum /= currencyRate.getSellRate();
                    break;
                }
            }
        }
        // Зачисляем на счет сумму
        deltaTo += sum;

        return new CoupleOfValues(transferDataDto.getCurrencyNameFrom(), deltaFrom, transferDataDto.getCurrencyNameTo(), deltaTo);
    }
}
