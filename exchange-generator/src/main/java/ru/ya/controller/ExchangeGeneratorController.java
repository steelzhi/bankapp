package ru.ya.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ya.model.CurrencyRates;
import ru.ya.service.ExchangeGeneratorService;

@RestController
public class ExchangeGeneratorController {
    @Autowired
    ExchangeGeneratorService exchangeGeneratorService;

    @Autowired
    Logger logger;

    @GetMapping("/exchange-rates")
    public CurrencyRates getCurrencyRates() {
            logger.atInfo().log("Getting currency rates");
        return exchangeGeneratorService.getCurrencyRates();
    }
}
