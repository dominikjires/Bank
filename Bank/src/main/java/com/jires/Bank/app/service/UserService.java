package com.jires.Bank.app.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.jires.Bank.app.service.ExchangeRateService.calculateExchange;

@Service
public class UserService {

    public static boolean accountExists(long id, String type) {
        String filePath = String.format("data/%d.txt", id);
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("Account file not found");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid account file format");
                }
                if (parts[0].equals(type)) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            throw new RuntimeException("Error loading accounts from file", e);
        }
    }

    public static void addAccount(long id, String type, double amount) {
        String filePath = String.format("data/%d.txt", id);
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("Account file not found");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            String line;
            boolean accountExists = false;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid account file format");
                }
                if (parts[0].equals(type)) {
                    accountExists = true;
                    break;
                }
            }
            if (!accountExists) {
                writer.write(type + "," + amount + "\n");
            } else {
                throw new IllegalArgumentException("Account of type already exists");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading accounts from file", e);
        }
    }


    public static boolean depositMoney(long id, String type, double amount) throws IOException {
        // Vstupní soubor, kde se mají provádět úpravy
        Path inputPath = Paths.get("data", id + ".txt");

        // Dočasný soubor pro ukládání upravených hodnot
        Path tempPath = Paths.get("data", id + "_temp.txt");

        try (BufferedReader reader = Files.newBufferedReader(inputPath);
             BufferedWriter writer = Files.newBufferedWriter(tempPath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(type)) {
                    double newAmount = Double.parseDouble(parts[1].trim()) + amount;
                    line = type + "," + newAmount;
                    writeToLog(id,"+",type,amount);
                }
                writer.write(line + System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Error updating account: " + e.getMessage());
            return false;
        }

        // Přejmenování dočasného souboru na původní soubor
        try {
            Files.move(tempPath, inputPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error renaming file: " + e.getMessage());
            return false;
        }

        return true;
    }


    public static int payment(long id, String type, double amount) throws IOException {
        // Soubor, kde se má vyhledat a nahradit hodnota
        File inputFile = new File("data/" + id + ".txt");
        // Dočasný soubor pro ukládání upravených hodnot
        File tempFile = new File("data/" + id + "_temp.txt");
        String found = null;
        double newAmount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            boolean foundType = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains(type)) {
                    String[] parts = line.split(",");
                    if (Double.parseDouble(parts[1].trim()) - amount < 0) {
                        break;
                    } else {
                        newAmount = Double.parseDouble(parts[1].trim()) - amount;
                        foundType = true;
                        found = type;
                        writeToLog(id,"-",found,amount);
                    }

                }
            }
            reader.close();
            if (!foundType) {
                BufferedReader reader2 = new BufferedReader(new FileReader(inputFile));
                // Procházejte soubor znovu a najděte řádek obsahující "CZK"
                while ((line = reader2.readLine()) != null) {
                    if (line.contains("CZK")) {
                        String[] parts = line.split(",");
                        double convertedAmount = calculateExchange(type, amount);
                        if (Double.parseDouble(parts[1].trim()) - convertedAmount < 0) {
                            return 0;
                        } else {
                            newAmount = Double.parseDouble(parts[1].trim()) - convertedAmount;
                            foundType = true;
                            found = "CZK";
                            writeToLog(id,"-",found,convertedAmount);
                        }
                    }
                }
                reader2.close();
            }
            if (foundType) {
                BufferedReader reader3 = new BufferedReader(new FileReader(inputFile));
                while ((line = reader3.readLine()) != null) {
                    if (line.contains(found)) {
                        line = found + "," + newAmount;
                    }
                    writer.write(line + System.lineSeparator());
                }
                reader3.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        if (inputFile.delete()) {
            if (!tempFile.renameTo(inputFile)) {
                System.err.println("Error renaming file");
                return 0;
            }
        } else {
            System.err.println("Error deleting file");
            return 0;
        }
        return 1;
    }

    private static void writeToLog(long id, String type, String currency, double amount) {
        String fileName = "data/log/" + id + ".txt";
        Path filePath = Paths.get(fileName);

        try {
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath.getParent());
                Files.createFile(filePath);
            }

            List<String> lines = new ArrayList<>(Files.readAllLines(filePath, StandardCharsets.UTF_8));

            if (lines.size() >= 5) {
                lines.remove(0);
            }
            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            lines.add(formattedDateTime + " " + type + " " + currency + " " + amount);

            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> readLog(long id) {
        String fileName = "data/log/" + id + ".txt";
        Path filePath = Paths.get(fileName);

        try {
            if (!Files.exists(filePath)) {
                return Collections.emptyList();
            }
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            Collections.reverse(lines);
            return lines;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static void main(String[] args) throws IOException {
        depositMoney(1, "CZK", 100);
        System.out.println(readLog(1));
    }
}


