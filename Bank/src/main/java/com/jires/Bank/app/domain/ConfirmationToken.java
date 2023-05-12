package com.jires.Bank.app.domain;

import java.time.LocalDateTime;


public class ConfirmationToken {
    private Long id;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private Boolean confirmed;

    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, Boolean confirmed, Long id) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.confirmed = confirmed;
        this.id=id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiredAt() {
        return expiresAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiresAt = expiredAt;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }
}
