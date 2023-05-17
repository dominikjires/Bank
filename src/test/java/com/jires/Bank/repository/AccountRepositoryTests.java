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
        List<Account> accounts = AccountRepository.findAccountsByUserId(999);
        Assertions.assertTrue(accounts.isEmpty());
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
}
