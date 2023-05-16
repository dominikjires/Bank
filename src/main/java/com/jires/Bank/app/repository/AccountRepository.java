package com.jires.Bank.app.repository;

import com.jires.Bank.app.domain.Account;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountRepository {

    // Finds accounts for the given user ID
    public static List<Account> findAccountsByUserId(long id) {
        List<Account> accounts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("data/" + id + ".txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Account account = new Account(parts[0], Double.parseDouble(parts[1]));
                accounts.add(account);
            }
        } catch (IOException e) {
            System.err.println("Error loading accounts from file: " + e.getMessage());
        }
        return accounts;
    }

    // Main method for testing
    public static void main(String[] args) {
        List<Account> accounts = new ArrayList<>();
        accounts = findAccountsByUserId(1);
        System.out.println(accounts);
    }
}