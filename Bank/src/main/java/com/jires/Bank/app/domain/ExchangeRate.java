package com.jires.Bank.app.domain;

public class ExchangeRate {
    private String country;
    private String currency;
    private String amount;
    private String code;
    private String exchangeRate;

    public ExchangeRate(String country, String currency, String amount, String code, String exchangeRate) {
        this.country = country;
        this.currency = currency;
        this.amount = amount;
        this.code = code;
        this.exchangeRate = exchangeRate;
    }

    public String getCountry() {
        return country;
    }

    public String getCurrency() {
        return currency;
    }

    public String getAmount() {
        return amount;
    }

    public String getCode() {
        return code;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }
}
