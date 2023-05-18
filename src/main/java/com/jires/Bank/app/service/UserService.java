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

    // Function to check if the account exists in the system
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

    // Function to add an account to the system
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

    // Function to deposit money into an account
    public static boolean depositMoney(long id, String type, double amount) throws IOException {
        Path inputPath = Paths.get("data", id + ".txt");
        Path tempPath = Paths.get("data", id + "_temp.txt");

        try (BufferedReader reader = Files.newBufferedReader(inputPath);
             BufferedWriter writer = Files.newBufferedWriter(tempPath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(type)) {
                    if((Double.parseDouble(parts[1].trim()) + amount) > Double.MAX_VALUE){
                        return false;
                    }
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
        try {
            Files.move(tempPath, inputPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error renaming file: " + e.getMessage());
            return false;
        }

        return true;
    }

    // Function to pay money from an account
    public static int payment(long id, String type, double amount) throws IOException {
        // Define input and temporary files
        File inputFile = new File("data/" + id + ".txt");
        File tempFile = new File("data/" + id + "_temp.txt");
        String found = null;
        double newAmount = 0;

        // Read input file and write to temporary file
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            boolean foundType = false;
            while ((line = reader.readLine()) != null) {
                // Search for the specified type in the input file
                if (line.contains(type)) {
                    // If the specified type is found, update the balance amount
                    String[] parts = line.split(",");
                    if (Double.parseDouble(parts[1].trim()) - amount < 0) {
                        break;
                    } else {
                        newAmount = Double.parseDouble(parts[1].trim()) - amount;
                        foundType = true;
                        found = type;
                        // Write the transaction to the log file
                        writeToLog(id,"-",found,amount);
                    }
                }
            }
            reader.close();
            // If the specified type is not found, search for CZK in the input file
            if (!foundType) {
                BufferedReader reader2 = new BufferedReader(new FileReader(inputFile));
                while ((line = reader2.readLine()) != null) {
                    if (line.contains("CZK")) {
                        String[] parts = line.split(",");
                        double convertedAmount = calculateExchange(type, amount);
                        if (Double.parseDouble(parts[1].trim()) - convertedAmount < 0) {
                            reader2.close();
                            return 0;
                        } else {
                            newAmount = Double.parseDouble(parts[1].trim()) - convertedAmount;
                            foundType = true;
                            found = "CZK";
                            // Write the transaction to the log file
                            writeToLog(id,"-",found,convertedAmount);
                        }
                    }
                }
                reader2.close();
            }
            // If the specified type or CZK is found, update the balance amount in the input file
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
        // Replace the input file with the temporary file
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

    // Write a log entry for a transaction
    public static void writeToLog(long id, String type, String currency, double amount) {
        // Construct the filename for the log file based on the account ID
        String fileName = "data/log/" + id + ".txt";
        Path filePath = Paths.get(fileName);

        try {
            // Create the directory and file if they don't already exist
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath.getParent());
                Files.createFile(filePath);
            }

            // Read all existing lines from the log file into a list
            List<String> lines = new ArrayList<>(Files.readAllLines(filePath, StandardCharsets.UTF_8));

            // If the log file has more than 5 lines, remove the oldest line
            if (lines.size() >= 5) {
                lines.remove(0);
            }

            // Get the current date and time
            LocalDateTime now = LocalDateTime.now();
            // Format the date and time as a string
            String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            // Add a new log entry to the list of lines
            lines.add(formattedDateTime + " " + type + " " + currency + " " + amount);

            // Write the updated list of lines back to the log file
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Read the log file for a given account ID
    public static List<String> readLog(long id) {
        // Construct the filename for the log file based on the account ID
        String fileName = "data/log/" + id + ".txt";
        Path filePath = Paths.get(fileName);

        try {
            // If the log file doesn't exist, return an empty list
            if (!Files.exists(filePath)) {
                return Collections.emptyList();
            }
            // Read all lines from the log file into a list
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            // Reverse the order of the lines so the most recent entries are first
            Collections.reverse(lines);
            return lines;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Main method for testing
    //public static void main(String[] args) throws IOException {
        //depositMoney(1, "CZK", 100);
        //System.out.println(readLog(1));
    //}
}


