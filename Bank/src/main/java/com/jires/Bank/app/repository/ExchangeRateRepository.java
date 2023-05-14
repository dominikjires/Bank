package com.jires.Bank.app.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.jires.Bank.app.domain.ExchangeRate;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.stereotype.Repository;

@Repository
public class ExchangeRateRepository {

    // Method to retrieve exchange rates from file
    public static List<ExchangeRate> getExchangeRates() throws IOException {
        List<ExchangeRate> exchangeRates = new ArrayList<>();

        // Read exchange rate data from file
        String[][] exchangeRateArray = readExchangeFile();

        // Create ExchangeRate object for each line and add to the list
        for (String[] exchangeRate : exchangeRateArray) {
            ExchangeRate exRate = new ExchangeRate(
                    exchangeRate[0],
                    exchangeRate[1],
                    exchangeRate[2],
                    exchangeRate[3],
                    exchangeRate[4]
            );
        }
        return exchangeRates;
    }

    // Method to get HTML content from a URL using HTTP GET request
    public static String getHtmlContent(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return EntityUtils.toString(response.getEntity());
        } catch (IOException ignored) {

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // Method to print a 2D string array
    public static void printArray(String[][] text) {
        for (int i = 0; i < text.length; i++) {
            for (int j = 0; j < text[i].length; j++) {
                System.out.print(text[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Method to retrieve exchange rate data for a specific currency
    public static String[] getExchangeRate(String currency) throws IOException {
        String[][] read = readExchangeFile();
        for (int i = 0; i < read.length; i++) {
            if (read[i][3].equalsIgnoreCase(currency)) {
                return read[i];
            }
        }
        return null;
    }

    // Method to read exchange rate data from file and return as 2D string array
    public static String[][] readExchangeFile() throws IOException {
        List<String> edit = Files.readAllLines(Paths.get("src/main/resources/exchangeRate.txt"));
        List<String[]> outputList = new ArrayList<>();

        // Iterate over lines of data and split into array of fields
        for (int i = 3; i < 34; i++) {
            String[] line = edit.get(i).split("\\|");
            outputList.add(line);
        }

        // Convert list of string arrays to 2D string array
        String[][] output = new String[outputList.size()][];
        for (int i = 0; i < outputList.size(); i++) {
            output[i] = outputList.get(i);
        }

        return output;
    }

    public static void main(String[] args) throws IOException {
        // Example usage: print exchange rate data to console
        printArray(readExchangeFile());
    }
}