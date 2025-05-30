package ru.ya.mapper;

import ru.ya.dto.BankAccountDto;
import ru.ya.dto.CoupleOfValuesDto;
import ru.ya.dto.TransferDataDto;
import ru.ya.dto.UserDto;
import ru.ya.model.CoupleOfValues;
import ru.ya.model.TransferData;

public class CoupleOfValuesMapper {
    private CoupleOfValuesMapper() {
    }

    public static CoupleOfValuesDto mapToCoupleOfValuesDto(CoupleOfValues coupleOfValues, TransferDataDto transferDataDto) {
        return new CoupleOfValuesDto(
                transferDataDto.getSenderId(),
                transferDataDto.getAccountNumberFrom(),
                coupleOfValues.getCurrencyNameFrom(),
                coupleOfValues.getValueFrom(),
                transferDataDto.getReceiverId(),
                transferDataDto.getAccountNumberTo(),
                coupleOfValues.getCurrencyNameTo(),
                coupleOfValues.getValueTo()
        );
    }
}
