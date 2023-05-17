package com.jires.Bank.service;

import com.jires.Bank.app.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

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
        boolean exists = UserService.accountExists(50, "USD");
        Assertions.assertFalse(exists);
    }

    @Test
    public void testAddAccount() {
        UserService.addAccount(2, "USD", 500);
        boolean exists = UserService.accountExists(1, "USD");
        Assertions.assertTrue(exists);
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
    public void testPaymentInvalidAccount() throws IOException {
        int result = UserService.payment(999, "CZK", 50);
        Assertions.assertEquals(0, result);
    }

    @Test
    public void testReadLogEmptyLog() {
        List<String> log = UserService.readLog(2);
        Assertions.assertNotNull(log);
        Assertions.assertTrue(log.isEmpty());
    }

}
