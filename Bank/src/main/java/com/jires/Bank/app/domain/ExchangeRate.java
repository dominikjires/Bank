package com.jires.Bank.app.domain;

public class ExchangeRate {
    private String country; // the country associated with the exchange rate
    private String currency; // the currency being exchanged
    private String amount; // the amount of currency being exchanged
    private String code; // the code representing the currency being exchanged
    private String exchangeRate; // the exchange rate between the two currencies

    // Constructor with parameters
    public ExchangeRate(String country, String currency, String amount, String code, String exchangeRate) {
        this.country = country;
        this.currency = currency;
        this.amount = amount;
        this.code = code;
        this.exchangeRate = exchangeRate;
    }

    // Getters and setters
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