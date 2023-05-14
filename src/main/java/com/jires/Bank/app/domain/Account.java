package com.jires.Bank.app.domain;

public class Account {

    // instance variables
    private String name;    // name of the account
    private double balance; // balance of the account

    // constructor
    public Account(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    // getter for name
    public String getName() {
        return name;
    }

    // getter for balance, with rounding to 3 decimal places
    public double getBalance() {
        return Math.round(balance * 1000.0) / 1000.0;
    }

    // toString method to display account information
    @Override
    public String toString() {
        return "currency = " + name + '\n' + ", balance = " + balance + '\n' + '}';
    }

}