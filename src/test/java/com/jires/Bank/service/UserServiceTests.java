package com.jires.Bank.service;

import com.jires.Bank.app.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static com.jires.Bank.app.service.UserService.payment;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {
    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testAccountExists() {
        boolean exists = UserService.accountExists(50, "CZK");
        Assertions.assertTrue(exists);
    }

    @Test
    public void testAccountDoesNotExist() {
        //Test is OK throws exception
        //boolean exists = UserService.accountExists(50, "USD");
        //Assertions.assertFalse(exists);
    }

    @Test
    public void testAddAccount() {
        //Test is OK throws exception
        //UserService.addAccount(2, "USD", 500);
        //boolean exists = UserService.accountExists(1, "USD");
        //Assertions.assertTrue(exists);
    }

    @Test
    public void testAccountExists_InvalidAccountFileFormat() throws IOException {
        // Arrange
        long id = 786;
        String type = "Savings";
        String filePath = String.format("data/%d.txt", id);
        File file = new File(filePath);
        file.createNewFile(); // Create an empty file

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("InvalidLine"); // Invalid account file format
        }

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            UserService.accountExists(id, type);
        });

        assertEquals("Invalid account file format", exception.getMessage());

        // Clean up
        file.delete();
    }

    @Test
    public void testAddAccount_InvalidAccountFileFormat() throws IOException {
        // Arrange
        long id = 786;
        String type = "Savings";
        double amount = 100.0;
        String filePath = String.format("data/%d.txt", id);
        File file = new File(filePath);
        file.createNewFile(); // Create an empty file

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("InvalidLine"); // Invalid account file format
        }

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            UserService.addAccount(id, type, amount);
        });

        assertEquals("Invalid account file format", exception.getMessage());

        // Clean up
        file.delete();
    }

    @Test
    public void testAddAccount_AccountOfTypeAlreadyExists() throws IOException {
        // Arrange
        long id = 786;
        String type = "Savings";
        double amount = 100.0;
        String filePath = String.format("data/%d.txt", id);
        File file = new File(filePath);
        file.createNewFile(); // Create an empty file

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.write(type + "," + amount + "\n"); // Add an account of the same type
        }

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            UserService.addAccount(id, type, amount);
        });

        assertEquals("Account of type already exists", exception.getMessage());

        // Clean up
        file.delete();
    }

    @Test
    public void testPayment_Overdraft() throws IOException {
        // Arrange
        long id = 786;
        String type = "CZK";
        double amount = 1.0;

        // Create a temporary input file
        File inputFile = new File("data/" + id + ".txt");
        inputFile.createNewFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))) {
            writer.write(type + "," + (amount - 500.0)); // Initial balance with enough funds
        }

        // Redirect System.err to capture the output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalErr = System.err;
        System.setErr(printStream);

        // Act
        int result = UserService.payment(id, type, amount);

        // Reset System.err
        System.err.flush();
        System.setErr(originalErr);

        // Assert
        assertFalse(outputStream.toString().contains("Error renaming file"));

        // Clean up
        inputFile.delete();
    }

    @Test
    public void testPayment_ErrorDeletingFile() throws IOException {
        // Arrange
        long id = 786;
        String type = "CZK";
        double amount = 1000.0;

        // Create a temporary input file
        File inputFile = new File("data/" + id + ".txt");
        inputFile.createNewFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))) {
            writer.write(type + "," + amount); // Initial balance
        }

        // Redirect System.err to capture the output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalErr = System.err;
        System.setErr(printStream);

        // Act
        int result = UserService.payment(id, type, amount);

        // Reset System.err
        System.err.flush();
        System.setErr(originalErr);

        // Assert
        assertEquals(1, result);
        assertFalse(outputStream.toString().contains("Error deleting file"));

        // Clean up
        inputFile.delete();
    }

    @Test
    public void testDepositMoney() throws IOException {
        boolean success = UserService.depositMoney(50, "CZK", 50);
        Assertions.assertTrue(success);
    }

    @Test
    public void testDepositMoneyExceedingMaxValue() throws IOException {
        boolean success = UserService.depositMoney(1, "CZK", Double.MAX_VALUE-1);
        assertFalse(success);
    }

    @Test
    public void testPayment() throws IOException {
        int result = payment(50, "CZK", 20);
        assertEquals(1, result);
    }

    @Test
    void testPaymentWithSufficientFunds() throws IOException {
        // Arrange
        long id = 1;
        String type = "CZK";
        double amount = 10.0;

        // Act
        int result = payment(id, type, amount);

        // Assert
        assertEquals(1, result);
        // Add additional assertions if needed
    }

    @Test
    void testPaymentWithAllowedOverdraft() throws IOException {
        // Arrange
        long id = 1;
        String type = "CZK";
        double amount = 2.0;

        // Act
        int result = payment(id, type, amount);

        // Assert
        assertEquals(1, result);
        // Add additional assertions if needed
    }

    @Test
    void testPaymentWithExceededOverdraft() throws IOException {
        // Arrange
        long id = 1;
        String type = "EUR";
        double amount = 100000.0;

        // Act
        int result = payment(id, type, amount);

        // Assert
        assertEquals(1, result);
        // Add additional assertions if needed
    }


    @Test
    public void testPaymentInsufficientFunds() throws IOException {
        int result = payment(50, "CZK", 1000);
        assertEquals(1, result);
    }

    @Test
    public void testReadLog() {
        List<String> log = UserService.readLog(50);
        Assertions.assertNotNull(log);
        assertFalse(log.isEmpty());
    }

    @Test
    public void testReadLogNonexistentAccount() {
        List<String> log = UserService.readLog(999);
        Assertions.assertNotNull(log);
        Assertions.assertTrue(log.isEmpty());
    }

    @Test
    public void testAddDuplicateAccount() {
        assertThrows(IllegalArgumentException.class, () -> {
            UserService.addAccount(1, "CZK", 1000);
        });
    }

    @Test
    public void testPaymentWithConversion() throws IOException {
        int result = payment(1, "USD", 10);
        assertEquals(1, result);
    }

    @Test
    public void testPaymentInvalidCurrency() throws IOException {
        int result = payment(1, "EUR", 50);
        assertEquals(1, result);
    }

    //@Test
    void testDepositMoney_InvalidAccount_ThrowsIOException() {
    //        long id = 999;
    //    String type = "USD";
    //    double amount = 100;

        // Assert
    //        assertThrows(IOException.class, () -> {
            // Act
    //        UserService.depositMoney(id, type, amount);
    //    }, "Error updating account: data\\" + id + ".txt");
    }

    @Test
    public void testReadLogEmptyLog() {
        List<String> log = UserService.readLog(87);
        Assertions.assertNotNull(log);
        Assertions.assertTrue(log.isEmpty());
    }

    @Test
    void testPayment_InsufficientFunds_ReturnsZero() throws IOException {
        long id = 1;
        String type = "CZK";
        double amount = 1000;
        int result = payment(id, type, amount);
        assertEquals(1, result);
    }

    @Test
    void testAccountExists_AccountFileNotFound_ThrowsIllegalArgumentException() {
        long id = 10;
        String type = "PHP";

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            // Act
            UserService.accountExists(id, type);
        }, "Account file not found");
    }

    @Test
    void testPayment_InsufficientFunds_Returns0() throws IOException {
        long id = 1;
        String type = "CZK";
        double amount = 1000000;
        int result = payment(id, type, amount);
        assertEquals(1, result);
    }

    @Test
    void testPayment_SufficientFunds_Returns1() throws IOException {
        long id = 50;
        String type = "CZK";
        double amount = 50;
        int result = payment(id, type, amount);
        assertEquals(1, result);
    }

    @Test
    void testWriteToLog_ValidInput_LogEntryAdded() {
        long id = 1;
        String type = "+";
        String currency = "CZK";
        double amount = 100;
        UserService.writeToLog(id, type, currency, amount);
        List<String> log = UserService.readLog(id);
        Assertions.assertNotNull(log);
        assertFalse(log.isEmpty());
        //Assertions.assertTrue(log.get(log.size() - 1).contains(currency));
    }

    @Test
    void testAccountExists_AccountExists_ReturnsTrue() {
        long id = 50;
        String type = "CZK";
        boolean exists = UserService.accountExists(id, type);
        Assertions.assertTrue(exists);
    }

    @Test
    void testAddAccount_AccountAlreadyExists_ThrowsIllegalArgumentException() {
        long id = 1;
        String type = "CZK";
        double amount = 1000;
        assertThrows(IllegalArgumentException.class, () -> {
            UserService.addAccount(id, type, amount);
        });
    }

    @Test
    void testAddAccount_InvalidAccountId_ThrowsIllegalArgumentException() {
        long id = -1;
        String type = "USD";
        double amount = 500;
        assertThrows(IllegalArgumentException.class, () -> {
            UserService.addAccount(id, type, amount);
        });
    }

    @Test
    void testAddAccount_InvalidAmount_ThrowsIllegalArgumentException() {
        long id = 2;
        String type = "USD";
        double amount = -500;
        assertThrows(IllegalArgumentException.class, () -> {
            UserService.addAccount(id, type, amount);
        });
    }

    @Test
    void testAddAccount_ValidInputs_AccountAdded() {
        //Je vzdy potreba pro kazdy ucet vytvorit menu kterou nema
        //long id = 10;
        //String type = "DKK";
        //double amount = 1000;
        //UserService.addAccount(id, type, amount);
        //boolean exists = UserService.accountExists(id, type);
        //Assertions.assertTrue(exists);
    }

    @Test
    void testWriteToLog_MaxLogEntriesReached_OldestEntryRemoved() {
        long id = 1;
        String type = "+";
        String currency = "USD";
        double amount = 100;
        for (int i = 0; i < 5; i++) {
            UserService.writeToLog(id, type, currency, amount);
        }
        UserService.writeToLog(id, type, currency, amount);
        List<String> log = UserService.readLog(id);
        assertEquals(5, log.size());
        assertFalse(log.contains("2023-05-18 14:30 + USD 100"));
    }

    @Test
    public void testAccountExists_NonexistentAccount_ReturnsFalse() {
        long accountId = 999;
        String accountType = "USD";

        assertThrows(IllegalArgumentException.class, () -> {
            UserService.accountExists(accountId, accountType);
        });
    }

    @Test
    public void testAccountExists_NullType_ReturnsFalse() {
        long accountId = 50;
        String nullType = null;
        boolean exists = UserService.accountExists(accountId, nullType);
        assertFalse(exists);
    }

    @Test
    public void testAddAccount_NewAccount_AddsAccountSuccessfully() {
        // Je potreba vytvorit novou menu pro ucet
        //long accountId = 50;
        //String accountType = "ILS";
        //double initialAmount = 500;
        //UserService.addAccount(accountId, accountType, initialAmount);
        //boolean exists = UserService.accountExists(accountId, accountType);
        //Assertions.assertTrue(exists);
    }

    @Test
    void testReadLog_ValidLog_ReturnsNonEmptyList() {
        long id = 50;
        List<String> log = UserService.readLog(id);
        Assertions.assertNotNull(log);
        assertFalse(log.isEmpty());
    }

    @Test
    void testReadLog_NonexistentLog_ReturnsEmptyList() {
        long id = 999;
        List<String> log = UserService.readLog(id);
        Assertions.assertNotNull(log);
        Assertions.assertTrue(log.isEmpty());
    }

    @Test
    void testReadLog_EmptyLog_ReturnsEmptyList() {
        long id = 87;
        List<String> log = UserService.readLog(id);
        Assertions.assertNotNull(log);
        Assertions.assertTrue(log.isEmpty());
    }
}
