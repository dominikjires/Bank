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

    @Test
    public void testSetConfirmed_SetsConfirmed() {
        ConfirmationToken token = new ConfirmationToken("token123", LocalDateTime.now(), LocalDateTime.now().plusHours(1), false, 1L);
        token.setConfirmed(true);
        Boolean confirmed = token.getConfirmed();
        Assertions.assertTrue(confirmed);
    }

    @Test
    public void testSetId_SetsId() {
        ConfirmationToken token = new ConfirmationToken("token123", LocalDateTime.now(), LocalDateTime.now().plusHours(1), false, 1L);
        token.setId(2L);
        Long id = token.getId();
        Assertions.assertEquals(2L, id);
    }

    @Test
    public void testSetExpiresAt_SetsExpiresAt() {
        ConfirmationToken token = new ConfirmationToken("token123", LocalDateTime.now(), LocalDateTime.now().plusHours(1), false, 1L);
        LocalDateTime newExpiresAt = LocalDateTime.now().plusHours(2);
        token.setExpiresAt(newExpiresAt);
        LocalDateTime expiresAt = token.getExpiresAt();
        Assertions.assertEquals(newExpiresAt, expiresAt);
    }

    @Test
    public void testSetToken_SetsToken() {
        ConfirmationToken token = new ConfirmationToken("token123", LocalDateTime.now(), LocalDateTime.now().plusHours(1), false, 1L);
        String newToken = "newToken123";
        token.setToken(newToken);
        String tokenString = token.getToken();
        Assertions.assertEquals(newToken, tokenString);
    }

    @Test
    public void testSetCreatedAt_SetsCreatedAt() {
        ConfirmationToken token = new ConfirmationToken("token123", LocalDateTime.now(), LocalDateTime.now().plusHours(1), false, 1L);
        LocalDateTime newCreatedAt = LocalDateTime.now().minusDays(1);
        token.setCreatedAt(newCreatedAt);
        LocalDateTime createdAt = token.getCreatedAt();
        Assertions.assertEquals(newCreatedAt, createdAt);
    }

    @Test
    public void testSetExpiredAt_SetsExpiredAt() {
        ConfirmationToken token = new ConfirmationToken("token123", LocalDateTime.now(), LocalDateTime.now().plusHours(1), false, 1L);
        LocalDateTime newExpiredAt = LocalDateTime.now().plusDays(1);
        token.setExpiredAt(newExpiredAt);
        LocalDateTime expiredAt = token.getExpiredAt();
        Assertions.assertEquals(newExpiredAt, expiredAt);
    }

    @Test
    public void testGetExpiredAt_ReturnsExpiredAt() {
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(1);
        ConfirmationToken token = new ConfirmationToken("token123", LocalDateTime.now(), LocalDateTime.now().plusHours(1), false, 1L);
        token.setExpiredAt(expiredAt);
        LocalDateTime tokenExpiredAt = token.getExpiredAt();
        Assertions.assertEquals(expiredAt, tokenExpiredAt);
    }
}
