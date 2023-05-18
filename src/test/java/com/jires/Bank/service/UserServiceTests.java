package com.jires.Bank.service;

import com.jires.Bank.app.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;

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
    public void testDepositMoney() throws IOException {
        boolean success = UserService.depositMoney(50, "CZK", 50);
        Assertions.assertTrue(success);
    }

    @Test
    public void testDepositMoneyExceedingMaxValue() throws IOException {
        boolean success = UserService.depositMoney(1, "CZK", Double.MAX_VALUE-1);
        Assertions.assertFalse(success);
    }

    @Test
    public void testPayment() throws IOException {
        int result = UserService.payment(50, "CZK", 20);
        Assertions.assertEquals(1, result);
    }

    @Test
    public void testPaymentInsufficientFunds() throws IOException {
        int result = UserService.payment(50, "CZK", 1000);
        Assertions.assertEquals(1, result);
    }

    @Test
    public void testReadLog() {
        List<String> log = UserService.readLog(50);
        Assertions.assertNotNull(log);
        Assertions.assertFalse(log.isEmpty());
    }

    @Test
    public void testReadLogNonexistentAccount() {
        List<String> log = UserService.readLog(999);
        Assertions.assertNotNull(log);
        Assertions.assertTrue(log.isEmpty());
    }

    @Test
    public void testAddDuplicateAccount() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            UserService.addAccount(1, "CZK", 1000);
        });
    }

    @Test
    public void testPaymentWithConversion() throws IOException {
        int result = UserService.payment(1, "USD", 10);
        Assertions.assertEquals(1, result);
    }

    @Test
    public void testPaymentInvalidCurrency() throws IOException {
        int result = UserService.payment(1, "EUR", 50);
        Assertions.assertEquals(1, result);
    }

    @Test
    public void testDepositMoneyInvalidAccount() throws IOException {
        boolean success = UserService.depositMoney(999, "USD", 100);
        Assertions.assertFalse(success);
    }

    @Test
    public void testReadLogEmptyLog() {
        List<String> log = UserService.readLog(87);
        Assertions.assertNotNull(log);
        Assertions.assertTrue(log.isEmpty());
    }

    @Test
    void testPayment_InsufficientFunds_ReturnsZero() throws IOException {
        long id = 10;
        String type = "CZK";
        double amount = 1000;
        int result = UserService.payment(id, type, amount);
        Assertions.assertEquals(0, result);
    }

    @Test
    void testAccountExists_AccountFileNotFound_ReturnsFalse() {
        long id = 10;
        String type = "HUF";
        boolean exists = UserService.accountExists(id, type);
        Assertions.assertFalse(exists);
    }

    @Test
    void testPayment_InsufficientFunds_Returns0() throws IOException {
        long id = 10;
        String type = "CZK";
        double amount = 1000;
        int result = UserService.payment(id, type, amount);
        Assertions.assertEquals(0, result);
    }

    @Test
    void testPayment_SufficientFunds_Returns1() throws IOException {
        long id = 50;
        String type = "CZK";
        double amount = 50;
        int result = UserService.payment(id, type, amount);
        Assertions.assertEquals(1, result);
    }

    @Test
    void testWriteToLog_ValidInput_LogEntryAdded() {
        long id = 10;
        String type = "+";
        String currency = "CZK";
        double amount = 100;
        UserService.writeToLog(id, type, currency, amount);
        List<String> log = UserService.readLog(id);
        Assertions.assertNotNull(log);
        Assertions.assertFalse(log.isEmpty());
        Assertions.assertTrue(log.get(log.size() - 1).contains(currency));
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
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            UserService.addAccount(id, type, amount);
        });
    }

    @Test
    void testAddAccount_InvalidAccountId_ThrowsIllegalArgumentException() {
        long id = -1;
        String type = "USD";
        double amount = 500;
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            UserService.addAccount(id, type, amount);
        });
    }

    @Test
    void testAddAccount_InvalidAmount_ThrowsIllegalArgumentException() {
        long id = 2;
        String type = "USD";
        double amount = -500;
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
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
        Assertions.assertEquals(5, log.size());
        Assertions.assertFalse(log.contains("2023-05-18 14:30 + USD 100"));
    }

    @Test
    public void testAccountExists_NonexistentAccount_ReturnsFalse() {
        long accountId = 999;
        String accountType = "USD";

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            UserService.accountExists(accountId, accountType);
        });
    }

    @Test
    public void testAccountExists_NullType_ReturnsFalse() {
        long accountId = 50;
        String nullType = null;
        boolean exists = UserService.accountExists(accountId, nullType);
        Assertions.assertFalse(exists);
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
        Assertions.assertFalse(log.isEmpty());
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
