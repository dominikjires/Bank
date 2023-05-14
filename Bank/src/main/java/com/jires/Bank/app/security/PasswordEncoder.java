package com.jires.Bank.app.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {

    // This is the main function that will be executed when the program is run
    public static void main(String[] args) {
        // Create a new instance of the BCryptPasswordEncoder class
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "heslo";

        // Encode the raw password using the encoder object
        String encodedPassword = encoder.encode(rawPassword);

        System.out.println(encodedPassword);
    }
}