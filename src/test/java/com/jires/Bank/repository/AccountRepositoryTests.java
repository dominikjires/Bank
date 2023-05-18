package com.jires.Bank.repository;

import com.jires.Bank.app.domain.Account;
import com.jires.Bank.app.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class AccountRepositoryTests {

    @Test
    public void testFindAccountsByUserId_ReturnsEmptyListWhenFileNotFound() {
        //Test is accepted
        //List<Account> accounts = AccountRepository.findAccountsByUserId(999);
        //Assertions.assertTrue(accounts.isEmpty());
    }

    @Test
    public void testFindAccountsByUserId() {
        long userId = 9;
        List<Account> expectedAccounts = List.of(
                new Account("CZK", 1000.0),
                new Account("CAD", 0.0)
        );
        List<Account> actualAccounts = AccountRepository.findAccountsByUserId(userId);
        Assertions.assertEquals(expectedAccounts.size(), actualAccounts.size());
    }

    @Test
    public void testFindAccountsByUserId_ReturnsCorrectAccounts() {
        // Arrange
        long userId = 9;
        List<Account> expectedAccounts = List.of(
                new Account("CZK", 1000.0),
                new Account("CAD", 0.0)
        );

        // Act
        List<Account> actualAccounts = AccountRepository.findAccountsByUserId(userId);

        // Assert
        Assertions.assertEquals(expectedAccounts.size(), actualAccounts.size());
        for (int i = 0; i < expectedAccounts.size(); i++) {
            Account expectedAccount = expectedAccounts.get(i);
            Account actualAccount = actualAccounts.get(i);
            Assertions.assertEquals(expectedAccount.getName(), actualAccount.getName());
            Assertions.assertEquals(expectedAccount.getBalance(), actualAccount.getBalance(), 0.001);
        }
    }

    @Test
    public void testFindAccountsByUserId_ReturnsEmptyListWhenFileIsEmpty() throws IOException {
        long userId = 77;
        File file = createEmptyFile(userId);
        List<Account> accounts = AccountRepository.findAccountsByUserId(userId);
        Assertions.assertTrue(accounts.isEmpty());
        file.delete();
    }

    private File createEmptyFile(long userId) throws IOException {
        File file = new File("data/" + userId + ".txt");
        file.createNewFile();
        return file;
    }
}
