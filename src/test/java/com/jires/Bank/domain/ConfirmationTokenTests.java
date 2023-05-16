package com.jires.Bank.domain;

import com.jires.Bank.app.domain.ConfirmationToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class ConfirmationTokenTests {
    @Test
    public void testGetId_ReturnsId() {
        ConfirmationToken token = new ConfirmationToken("token123", LocalDateTime.now(), LocalDateTime.now().plusHours(1), false, 1L);
        Long id = token.getId();
        Assertions.assertEquals(1L, id);
    }

    @Test
    public void testGetExpiresAt_ReturnsExpiresAt() {
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(1);
        ConfirmationToken token = new ConfirmationToken("token123", LocalDateTime.now(), expiresAt, false, 1L);
        LocalDateTime tokenExpiresAt = token.getExpiresAt();
        Assertions.assertEquals(expiresAt, tokenExpiresAt);
    }

    @Test
    public void testGetToken_ReturnsToken() {
        ConfirmationToken token = new ConfirmationToken("token123", LocalDateTime.now(), LocalDateTime.now().plusHours(1), false, 1L);
        String tokenString = token.getToken();
        Assertions.assertEquals("token123", tokenString);
    }

    @Test
    public void testGetCreatedAt_ReturnsCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now();
        ConfirmationToken token = new ConfirmationToken("token123", createdAt, LocalDateTime.now().plusHours(1), false, 1L);
        LocalDateTime tokenCreatedAt = token.getCreatedAt();
        Assertions.assertEquals(createdAt, tokenCreatedAt);
    }

    @Test
    public void testGetConfirmed_ReturnsConfirmed() {
        ConfirmationToken token = new ConfirmationToken("token123", LocalDateTime.now(), LocalDateTime.now().plusHours(1), true, 1L);
        Boolean confirmed = token.getConfirmed();
        Assertions.assertTrue(confirmed);
    }
}
