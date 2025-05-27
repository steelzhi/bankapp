package ru.ya.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CurrencyRates {
    private List<CurrencyRate> currencyRatesList = new ArrayList<>();

    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CurrencyRate {
        double cBValue;
        double tolerance;
        double sellRate;
        double buyRate;
        String currencyName;

        public CurrencyRate(double value, double tolerance, String currencyName) {
            this.cBValue = value;
            this.tolerance = tolerance;
            this.currencyName = currencyName;
            setSellRate();
            setBuyRate();
        }

        public void setSellRate() {
            sellRate = cBValue * (1 + tolerance);
        }

        public void setBuyRate() {
            buyRate = cBValue * (1 - tolerance);
        }

        public double getSellRate() {
            return sellRate;
        }

        public double getBuyRate() {
            return buyRate;
        }

        public String getCurrencyName() {
            return currencyName;
        }
    }
}
