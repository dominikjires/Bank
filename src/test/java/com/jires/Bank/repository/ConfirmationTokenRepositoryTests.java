package com.jires.Bank.repository;

import com.jires.Bank.app.domain.ConfirmationToken;
import com.jires.Bank.app.repository.ConfirmationTokenRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ConfirmationTokenRepositoryTests {

    private static final String TOKEN_FILE = "data/tokens.txt";
    @Test
    public void testFindByToken_TokenFound() throws IOException {
        String tokenToFind = "token123";
        String existingTokenData = "1," + tokenToFind + ",2023-05-17T10:30:00,2023-05-20T10:30:00,true,1";
        appendDataToFile(existingTokenData);
        ConfirmationTokenRepository repository = new ConfirmationTokenRepository();
        Optional<ConfirmationToken> optionalToken = repository.findByToken(tokenToFind);
        assertTrue(optionalToken.isPresent());
        ConfirmationToken foundToken = optionalToken.get();
        assertEquals(tokenToFind, foundToken.getToken());
        assertEquals(LocalDateTime.of(2023, 5, 17, 10, 30, 00), foundToken.getCreatedAt());
        assertEquals(LocalDateTime.of(2023, 5, 20, 10, 30, 00), foundToken.getExpiresAt());
        assertTrue(foundToken.getConfirmed());
        assertEquals(1L, foundToken.getId());
    }

    private void appendDataToFile(String data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TOKEN_FILE, true))) {
            writer.write(data);
            writer.newLine();
        }
    }

    @Test
    public void testFindByToken_TokenNotFound() throws IOException {
        String existingTokenData = "1,token123,2023-05-17T10:30:00,2023-05-20T10:30:00,true,1";
        appendDataToFile(existingTokenData);
        String tokenToFind = "nonexistent_token";
        ConfirmationTokenRepository repository = new ConfirmationTokenRepository();
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            Optional<ConfirmationToken> optionalToken = repository.findByToken(tokenToFind);
            assertFalse(optionalToken.isPresent());
        });
    }

    @Test
    public void testGetToken_TokenNotFound() throws IOException {
        String existingTokenData = "token123,2023-05-17T10:30:00,2023-05-20T10:30:00,true,1";
        appendDataToFile(existingTokenData);
        String tokenToFind = "nonexistent_token";
        ConfirmationTokenRepository repository = new ConfirmationTokenRepository();
        Optional<ConfirmationToken> optionalToken = repository.getToken(tokenToFind);
        assertFalse(optionalToken.isPresent());
    }
    @Test
    public void testGetToken_TokenFound() throws IOException {
        String tokenToFind = "token123";
        String existingTokenData = tokenToFind + ",2023-05-17T10:30:00,2023-05-20T10:30:00,true,1";
        appendDataToFile(existingTokenData);
        ConfirmationTokenRepository repository = new ConfirmationTokenRepository();
        Optional<ConfirmationToken> optionalToken = repository.getToken(tokenToFind);
        assertTrue(optionalToken.isPresent());
        ConfirmationToken foundToken = optionalToken.get();
        assertEquals(tokenToFind, foundToken.getToken());
    }



}
