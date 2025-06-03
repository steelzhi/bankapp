package ru.ya.mapper;

import ru.ya.dto.BankAccountDto;
import ru.ya.dto.TransferDataDto;
import ru.ya.dto.UserDto;
import ru.ya.model.TransferData;

public class TransferDataMapper {
    private TransferDataMapper() {
    }

    public static TransferDataDto mapToTransferDataDto(TransferData transferData, UserDto userDto) {
        String currencyNameFrom = "";
        String currencyNameTo = "";

        for (BankAccountDto bankAccountDto : userDto.getBankAccountDtoList()) {
            if (bankAccountDto.getAccountNumber().equals(transferData.getAccountNumberFrom())) {
                currencyNameFrom = bankAccountDto.getCurrency().name();
            }
            if (transferData.getCurrencyNameTo() == null) {
                if (bankAccountDto.getAccountNumber().equals(transferData.getAccountNumberTo())) {
                    currencyNameTo = bankAccountDto.getCurrency().name();
                }
            } else {
                currencyNameTo = transferData.getCurrencyNameTo();
            }
        }

        return new TransferDataDto(
                transferData.getSenderId(),
                transferData.getReceiverId(),
                transferData.getAccountNumberFrom(),
                currencyNameFrom,
                transferData.getAccountNumberTo(),
                currencyNameTo,
                transferData.getSum()
                );
    }

    public static TransferDataDto mapToTransferDataDto(TransferData transferData, String currencyNameFrom, String currencyNameTo) {
        return new TransferDataDto(
                transferData.getSenderId(),
                transferData.getReceiverId(),
                transferData.getAccountNumberFrom(),
                currencyNameFrom,
                transferData.getAccountNumberTo(),
                currencyNameTo,
                transferData.getSum()
        );
    }
}
