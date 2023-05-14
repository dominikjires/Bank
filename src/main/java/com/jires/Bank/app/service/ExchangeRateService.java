package com.jires.Bank.app.service;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static com.jires.Bank.app.repository.ExchangeRateRepository.getHtmlContent;
import static com.jires.Bank.app.repository.ExchangeRateRepository.getExchangeRate;

@Service
@EnableScheduling
public class ExchangeRateService {

    private final String FILENAME = "exchangeRate.txt";
    private final String FILEPATH = "src/main/resources/";
    private final String URL = "https://www.cnb.cz/cs/financni-trhy/devizovy-trh/kurzy-devizoveho-trhu/kurzy-devizoveho-trhu/denni_kurz.txt";

    // This method refreshes the exchange rate file on weekdays at 3 PM
    @Scheduled(cron = "0 00 15 ? * MON-FRI", zone = "Europe/Prague")
    public void refreshExchangeFile() throws IOException {
        // Get the HTML content of the exchange rate website
        String htmlContent = getHtmlContent(URL);

        try {
            // Get the current timestamp
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);

            // Write the HTML content and the timestamp to the exchange rate file
            Path filePath = Paths.get(FILEPATH + FILENAME);
            FileWriter writer = new FileWriter(filePath.toFile(), false); // false for override whole file as new one
            writer.write(timestamp + "\n");
            writer.write(htmlContent + "\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("Error while writing exchange rate file: " + e.getMessage());
        }
    }

    // This method calculates the exchange rate between a given currency and CZK
    public static double calculateExchange(String currencyFrom, double amount) throws IOException {
        if (currencyFrom.equalsIgnoreCase("CZK")) {
            return amount;
        }

        // Get the exchange rate information for the given currency
        String[] exchangeInfo = getExchangeRate(currencyFrom);
        double exAmount = Double.parseDouble(exchangeInfo[2].replaceAll(",", ".")); // amount to CZK
        double exRate = Double.parseDouble(exchangeInfo[4].replaceAll(",", ".")); // exchange rate

        // Calculate the exchange rate
        return (amount * exRate) / exAmount;
    }

    // This is a testing method to check the calculateExchange() method
    public static void main(String[] args) throws IOException {
        System.out.println(calculateExchange("USD", 1000));
    }
}