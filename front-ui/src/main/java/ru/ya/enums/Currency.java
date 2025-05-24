package ru.ya.enums;

public enum Currency {
    RUB("Рубли"),
    USD("Доллары"),
    CNY("Юани");

    private String currencyName;

    Currency(String currencyName) {
        this.currencyName = currencyName;
    }

    public static String getCurrencyName(Currency currency) {
        return currency.currencyName;
    }
}
