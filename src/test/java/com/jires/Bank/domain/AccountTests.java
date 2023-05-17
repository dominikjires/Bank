package com.jires.Bank.domain;

import com.jires.Bank.app.domain.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AccountTests {
    @Test
    public void testGetName_ReturnsName() {
        Account account = new Account("John Doe", 1000.0);
        String name = account.getName();
        Assertions.assertEquals("John Doe", name);
    }

    @Test
    public void testGetBalance_ReturnsRoundedBalance() {
        Account account = new Account("John Doe", 1000.123456);
        double balance = account.getBalance();
        Assertions.assertEquals(1000.123, balance);
    }

    @Test
    public void testToString_ReturnsFormattedString() {
        Account account = new Account("John Doe", 1000.0);
        String accountString = account.toString();
        String expectedString = "currency = John Doe\n, balance = 1000.0\n}";
        Assertions.assertEquals(expectedString, accountString);
    }
}
