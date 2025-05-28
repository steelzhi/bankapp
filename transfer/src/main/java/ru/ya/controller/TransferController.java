package ru.ya.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ya.dto.CoupleOfValuesDto;
import ru.ya.dto.TransferDataDto;
import ru.ya.enums.ErrorOperation;
import ru.ya.mapper.CoupleOfValuesMapper;
import ru.ya.model.*;
import ru.ya.service.TransferService;
import ru.ya.util.ResponseFromModule;

@RestController
public class TransferController {
    @Autowired
    TransferService transferService;

    @Autowired
    Logger logger;

    @Autowired
    ResponseFromModule responseFromModule;

    @PostMapping("/transfer")
    public String getConvertedSum(@RequestBody TransferData transferData) {
        TransferDataDto transferDataDto = responseFromModule.getTransferDataDtoResponseFromModuleAccounts("/is-possible-to-transfer", transferData);
        if (transferDataDto == null) {
            return responseFromModule.getStringResponseFromModuleNotifications("/notification/error", new Operation(ErrorOperation.NOT_ENOUGH_MONEY_TO_TRANSFER));
        }

        boolean isOpSuspicious = responseFromModule.getBooleanResponseForSuspiciousOpsFromModuleBlocker("/check-operation");
        if (isOpSuspicious) {
            return responseFromModule.getStringResponseFromModuleNotifications("/notification/error", new Operation(ErrorOperation.SUSPICIOUS_OPERATION));
        }

       logger.atInfo().log("Произвожу конвертацию суммы " + transferDataDto.getSum() + " из "
                            + transferDataDto.getCurrencyNameFrom() + " в " + transferDataDto.getCurrencyNameTo());
        CoupleOfValues coupleOfValues = responseFromModule.getCoupleOfValuesResponseFromModuleExchange("/exchange-values", transferDataDto);
        CoupleOfValuesDto coupleOfValuesDto = CoupleOfValuesMapper.mapToCoupleOfValuesDto(coupleOfValues, transferDataDto);
        return responseFromModule.getStringResponseFromModuleAccounts("/transfer", coupleOfValuesDto);
    }
}
