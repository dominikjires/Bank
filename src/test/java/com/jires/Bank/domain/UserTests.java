package com.jires.Bank.domain;

import com.jires.Bank.app.domain.Account;
import com.jires.Bank.app.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class UserTests {
    @Test
    public void testGetId_ReturnsId() {
        User user = new User(1, "John", "Doe", "john.doe@example.com", "password123");

        long id = user.getId();

        Assertions.assertEquals(1, id);
    }

    @Test
    public void testGetFirstName_ReturnsFirstName() {
        User user = new User(1, "John", "Doe", "john.doe@example.com", "password123");

        String firstName = user.getFirstName();

        Assertions.assertEquals("John", firstName);
    }

    @Test
    public void testGetLastName_ReturnsLastName() {
        User user = new User(1, "John", "Doe", "john.doe@example.com", "password123");
        String lastName = user.getLastName();
        Assertions.assertEquals("Doe", lastName);
    }

    @Test
    public void testGetEmail_ReturnsEmail() {
        User user = new User(1, "John", "Doe", "john.doe@example.com", "password123");
        String email = user.getEmail();
        Assertions.assertEquals("john.doe@example.com", email);
    }

    @Test
    public void testGetPassword_ReturnsPassword() {
        User user = new User(1, "John", "Doe", "john.doe@example.com", "password123");
        String password = user.getPassword();
        Assertions.assertEquals("password123", password);
    }

    @Test
    public void testToString_ReturnsStringRepresentation() {
        User user = new User(1, "John", "Doe", "john.doe@example.com", "password123");
        String userString = user.toString();
        Assertions.assertEquals("1 John Doe john.doe@example.com password123", userString);
    }

    @Test
    public void testLoadAccountsFromFile_LoadsAccounts() {
        File file = new File("data/100.txt");
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("Savings,1000.0");
            writer.println("Checking,500.0");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        User user = new User(100, "John", "Doe", "john.doe@example.com", "password123");
        user.loadAccountsFromFile("data/100.txt");

        List<Account> accounts = user.getAccounts();

        Assertions.assertEquals(4, accounts.size());
        Account account1 = accounts.get(0);
        Assertions.assertEquals("Savings", account1.getName());
        Assertions.assertEquals(1000.0, account1.getBalance());

        Account account2 = accounts.get(1);
        Assertions.assertEquals("Checking", account2.getName());
        Assertions.assertEquals(500.0, account2.getBalance());

        file.delete();
    }
}
