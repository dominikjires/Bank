package com.jires.Bank.app.domain;

// This is an interface for an email sender
public interface EmailSender {
    // This function sends an email to the given recipient
    // with the given email content
    void send(String to, String email);
}