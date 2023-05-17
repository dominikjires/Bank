package com.jires.Bank.app.domain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class User {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<Account> accounts;

    // Constructor with parameters
    public User(long id, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        accounts = new ArrayList<>();
        loadAccountsFromFile("data/" + id + ".txt"); // Load the user's accounts from a file
    }

    // Getters and setters
    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String toString() { // Override the toString method to return a string representation of the user
        StringBuilder builder = new StringBuilder();
        builder.append(this.id).append(" ").append(this.firstName).append(" ").append(this.lastName).append(" ").append(this.email).append(" ").append(this.password);
        return builder.toString();
    }

    public void loadAccountsFromFile(String filename) { // Load accounts from a file
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Account account = new Account(parts[0], Double.parseDouble(parts[1]));
                accounts.add(account); // Add the account to the user's list of accounts
            }
        } catch (IOException e) {
            System.err.println("Error loading accounts from file: " + e.getMessage());
        }
    }

    public List<Account> getAccounts() {
        return accounts;
    }
}