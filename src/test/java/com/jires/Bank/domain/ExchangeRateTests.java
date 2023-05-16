package com.jires.Bank.domain;

import com.jires.Bank.app.domain.ExchangeRate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ExchangeRateTests {
    @Test
    public void testGetCountry_ReturnsCountry() {
        ExchangeRate exchangeRate = new ExchangeRate("United States", "USD", "100", "USD", "1.00");
        String country = exchangeRate.getCountry();
        Assertions.assertEquals("United States", country);
    }

    @Test
    public void testGetCurrency_ReturnsCurrency() {
        ExchangeRate exchangeRate = new ExchangeRate("United States", "USD", "100", "USD", "1.00");
        String currency = exchangeRate.getCurrency();
        Assertions.assertEquals("USD", currency);
    }

    @Test
    public void testGetAmount_ReturnsAmount() {
        ExchangeRate exchangeRate = new ExchangeRate("United States", "USD", "100", "USD", "1.00");
        String amount = exchangeRate.getAmount();
        Assertions.assertEquals("100", amount);
    }

    @Test
    public void testGetCode_ReturnsCode() {
        ExchangeRate exchangeRate = new ExchangeRate("United States", "USD", "100", "USD", "1.00");
        String code = exchangeRate.getCode();
        Assertions.assertEquals("USD", code);
    }

    @Test
    public void testGetExchangeRate_ReturnsExchangeRate() {
        ExchangeRate exchangeRate = new ExchangeRate("United States", "USD", "100", "USD", "1.00");
        String exchangeRateValue = exchangeRate.getExchangeRate();
        Assertions.assertEquals("1.00", exchangeRateValue);
    }
}
