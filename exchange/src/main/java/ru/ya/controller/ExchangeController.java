package ru.ya.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ya.dto.TransferDataDto;
import ru.ya.model.*;
import ru.ya.service.ExchangeService;

@RestController
public class ExchangeController {
    @Autowired
    ExchangeService exchangeService;

    @Autowired
    Logger logger;

    @PostMapping("/exchange-values")
    @ResponseBody
    public CoupleOfValues getConvertedSum(@RequestBody TransferDataDto transferDataDto) {
        logger.atInfo().log("Произвожу конвертацию суммы " + transferDataDto.getSum() + " из "
                            + transferDataDto.getCurrencyNameFrom() + " в " + transferDataDto.getCurrencyNameTo());
        return exchangeService.getConvertedSum(transferDataDto);
    }
}
