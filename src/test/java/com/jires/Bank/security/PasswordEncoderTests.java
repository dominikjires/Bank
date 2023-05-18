package com.jires.Bank.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordEncoderTests {
    @Test
    void encodePassword_ValidPassword_EncodesSuccessfully() {
        // Arrange
        String rawPassword = "heslo1234543242322432";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Act
        String encodedPassword = encoder.encode(rawPassword);

        // Assert
        Assertions.assertNotNull(encodedPassword);
        Assertions.assertNotEquals(rawPassword, encodedPassword);
        assertTrue(encoder.matches(rawPassword, encodedPassword));
    }

    @Test
    public void testEncodePassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "heslo";
        String encodedPassword = encoder.encode(rawPassword);
        assertTrue(encoder.matches(rawPassword, encodedPassword));
    }

    @Test
    public void testEncodeEmptyPassword() {
        // Testuje, zda prázdné heslo je správně zakódováno
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "";
        String encodedPassword = encoder.encode(rawPassword);
        assertTrue(encoder.matches(rawPassword, encodedPassword));
    }

    @Test
    public void testMatchPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "heslo";
        String encodedPassword = encoder.encode(rawPassword);
        assertTrue(encoder.matches(rawPassword, encodedPassword));
    }

    @Test
    public void testMismatchPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "heslo";
        String encodedPassword = encoder.encode(rawPassword);
        assertFalse(encoder.matches("spatneHeslo", encodedPassword));
    }
}
