package com.jires.Bank.app.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "panda";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword);
    }
}