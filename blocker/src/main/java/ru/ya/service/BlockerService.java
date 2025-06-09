package ru.ya.service;

import org.springframework.stereotype.Service;

@Service
public class BlockerService {
    public static final String CASH_MODULE = "cash";
    public static final String TRANSFER_MODULE = "transfer";
    private static final double SUSPICIOUS_PROBABILITY_FOR_CASH = 0.3;
    private static final double SUSPICIOUS_PROBABILITY_FOR_TRANSFER = 0.25;

    public Boolean isOperationSuspicious(String requesterModuleName) {
        double randomValue = Math.random();
        return switch (requesterModuleName) {
            case CASH_MODULE -> randomValue < SUSPICIOUS_PROBABILITY_FOR_CASH;
            case TRANSFER_MODULE -> randomValue < SUSPICIOUS_PROBABILITY_FOR_TRANSFER;
            default -> null;
        };
    }

}
