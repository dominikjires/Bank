package com.jires.Bank.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTests {
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
        Assertions.assertTrue(encoder.matches(rawPassword, encodedPassword));
    }
}
