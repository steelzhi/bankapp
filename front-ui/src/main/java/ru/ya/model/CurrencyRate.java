/*
package ru.ya.model;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class CurrencyRate {
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
*/
