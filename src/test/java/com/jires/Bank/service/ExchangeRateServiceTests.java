package com.jires.Bank.service;

import com.jires.Bank.app.domain.ExchangeRate;
import com.jires.Bank.app.service.ExchangeRateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ExchangeRateServiceTests {
    private final ExchangeRateService exchangeService = new ExchangeRateService();

    @Test
    public void testCalculateExchangeSameCurrency() throws IOException {
        double result = exchangeService.calculateExchange("CZK", 1000);
        Assertions.assertEquals(1000, result);
    }

    @Test
    public void testCalculateExchangeValidCurrency() throws IOException {
        double result = exchangeService.calculateExchange("USD", 100);
        Assertions.assertTrue(result > 0);
    }

    @Test
    public void testCalculateExchangeInvalidCurrency() throws IOException {
        double result = 0;

        try {
            result = exchangeService.calculateExchange("XYZ", 100);
        } catch (NullPointerException e) {
            return;
        }

        Assertions.assertEquals(0, result);
    }

    @Test
    public void testCalculateExchangeZeroAmount() throws IOException {
        double result = exchangeService.calculateExchange("EUR", 0);
        Assertions.assertEquals(0, result);
    }

    @Test
    public void testCalculateExchangeNegativeAmount() throws IOException {
        double result = exchangeService.calculateExchange("GBP", -100);
        Assertions.assertTrue(result < 0);
    }
}
