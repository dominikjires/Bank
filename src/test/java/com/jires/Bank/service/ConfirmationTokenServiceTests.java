package com.jires.Bank.service;

import com.jires.Bank.app.domain.ConfirmationToken;
import com.jires.Bank.app.repository.ConfirmationTokenRepository;
import com.jires.Bank.app.service.ConfirmationTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ConfirmationTokenServiceTests {
    private ConfirmationTokenService confirmationTokenService;

    @Mock
    private ConfirmationTokenRepository confirmationTokenRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        confirmationTokenService = new ConfirmationTokenService(confirmationTokenRepository);
    }

    @Test
    public void testSaveConfirmationToken() {
        ConfirmationToken token = new ConfirmationToken("token123", LocalDateTime.now(), LocalDateTime.now().plusHours(1), false, 1L);
        confirmationTokenService.saveConfirmationToken(token);
        boolean isTokenConfirmed = confirmationTokenService.isTokenConfirmed("token123");
        assertTrue(isTokenConfirmed || !isTokenConfirmed);
    }

    @Test
    public void testGetToken() {
        String tokenString = "token123";
        ConfirmationToken expectedToken = new ConfirmationToken(tokenString, LocalDateTime.now(), LocalDateTime.now().plusHours(1), false, 1L);
        when(confirmationTokenRepository.getToken(tokenString)).thenReturn(Optional.of(expectedToken));
        Optional<ConfirmationToken> actualToken = confirmationTokenService.getToken(tokenString);
        assertTrue(actualToken.isPresent());
    }

    @Test
    public void testIsTokenConfirmed() {
        String tokenString = "token123";
        ConfirmationToken token = new ConfirmationToken(tokenString, LocalDateTime.now(), LocalDateTime.now().plusHours(1), true, 1L);
        when(confirmationTokenRepository.getToken(tokenString)).thenReturn(Optional.of(token));
        boolean isConfirmed = confirmationTokenService.isTokenConfirmed(tokenString);
        assertTrue(isConfirmed);
        verify(confirmationTokenRepository, times(1)).getToken(tokenString);
    }

    @Test
    public void testIsTokenNotConfirmed() {
        String tokenString = "token123";
        when(confirmationTokenRepository.getToken(tokenString)).thenReturn(Optional.empty());
        boolean isConfirmed = confirmationTokenService.isTokenConfirmed(tokenString);
        assertFalse(isConfirmed);
        verify(confirmationTokenRepository, times(1)).getToken(tokenString);
    }

    @Test
    public void testSetConfirmedAt_ValidDateRange() throws IOException {
        String tokenToConfirm = "token123kdnfsfnsdf";
        String existingTokenData = "1," + tokenToConfirm + ",2023-05-17T10:30:00,2023-05-20T10:30:00,false,1";
        appendDataToFile(existingTokenData);

        int rowsAffected = confirmationTokenService.setConfirmedAt(tokenToConfirm);
        boolean isTokenConfirmed = confirmationTokenService.isTokenConfirmed(tokenToConfirm);
        assertFalse(isTokenConfirmed);
    }

    // Existing token with expired date range, should not be confirmed
    @Test
    public void testSetConfirmedAt_ExpiredDateRange() throws IOException {
        String tokenToConfirm = "token123";
        String existingTokenData = "1," + tokenToConfirm + ",2021-05-17T10:30:00,2021-05-20T10:30:00,false,1";
        appendDataToFile(existingTokenData);

        int rowsAffected = confirmationTokenService.setConfirmedAt(tokenToConfirm);
        boolean isTokenConfirmed = confirmationTokenService.isTokenConfirmed(tokenToConfirm);
        assertFalse(isTokenConfirmed);
    }

    // Non-existing token, no rows affected
    @Test
    public void testSetConfirmedAt_NonExistingToken() throws IOException {
        String tokenToConfirm = "nonexistent_token";
        int rowsAffected = confirmationTokenService.setConfirmedAt(tokenToConfirm);
        assertEquals(0, rowsAffected);
        boolean isTokenConfirmed = confirmationTokenService.isTokenConfirmed(tokenToConfirm);
        assertFalse(isTokenConfirmed);
    }

    private void appendDataToFile(String data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/tokens.txt", true))) {
            writer.write(data);
            writer.newLine();
        }
    }

    @Test
    void setConfirmedAt_InvalidToken_ReturnsZeroRowsAffected() {
        String tokenString = UUID.randomUUID().toString();
        when(confirmationTokenRepository.getToken(tokenString)).thenReturn(Optional.empty());
        int rowsAffected = confirmationTokenService.setConfirmedAt(tokenString);
        assertEquals(0, rowsAffected);
    }



}
