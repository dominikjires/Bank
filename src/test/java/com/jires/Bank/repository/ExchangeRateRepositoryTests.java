package com.jires.Bank.repository;

import com.jires.Bank.app.domain.ExchangeRate;
import com.jires.Bank.app.repository.ExchangeRateRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExchangeRateRepositoryTests {
    @Test
    public void testGetExchangeRates() throws IOException {
        List<ExchangeRate> exchangeRates = ExchangeRateRepository.getExchangeRates();
        assertNotNull(exchangeRates);
        assertFalse(exchangeRates.isEmpty());
    }

    @Test
    public void testGetHtmlContent() {
        String url = "http://example.com";
        String htmlContent = ExchangeRateRepository.getHtmlContent(url);
        assertNotNull(htmlContent);
        assertFalse(htmlContent.isEmpty());
    }

    @Test
    public void testReadExchangeFile() throws IOException {
        String[][] exchangeRateArray = ExchangeRateRepository.readExchangeFile();
        assertNotNull(exchangeRateArray);
        assertTrue(exchangeRateArray.length > 0);
    }

    @Test
    public void testGetExchangeRate_CurrencyPresent() throws IOException {
        String currency = "USD";
        String[] exchangeRate = ExchangeRateRepository.getExchangeRate(currency);
        assertNotNull(exchangeRate);
    }

    // Test for getExchangeRate method when currency is not present
    @Test
    public void testGetExchangeRate_CurrencyNotPresent() throws IOException {
        String currency = "XYZ";
        String[] exchangeRate = ExchangeRateRepository.getExchangeRate(currency);
        assertNull(exchangeRate);
    }

    @Test
    public void testGetExchangeRate_CurrencyPresent_CaseInsensitive() throws IOException {
        String currency = "usd";
        String[] exchangeRate = ExchangeRateRepository.getExchangeRate(currency);
        assertNotNull(exchangeRate);
    }

    @Test
    public void testGetExchangeRate_CurrencyNull() throws IOException {
        String currency = null;
        String[] exchangeRate = ExchangeRateRepository.getExchangeRate(currency);
        assertNull(exchangeRate);
    }

    @Test
    public void testGetExchangeRate_CurrencyEmpty() throws IOException {
        String currency = "";
        String[] exchangeRate = ExchangeRateRepository.getExchangeRate(currency);
        assertNull(exchangeRate);
    }
}
