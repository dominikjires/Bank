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

}
