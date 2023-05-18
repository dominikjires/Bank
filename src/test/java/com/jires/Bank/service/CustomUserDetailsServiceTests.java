package com.jires.Bank.service;

import com.jires.Bank.app.domain.Account;
import com.jires.Bank.app.domain.User;
import com.jires.Bank.app.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CustomUserDetailsServiceTests {
    private CustomUserDetailsService userDetailsService;
    private User user;

    @BeforeEach
    public void setup() {
        user = new User(1L, "John", "Doe", "john@example.com", "password");
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account("account1", 100.0));
        accounts.add(new Account("account2", 200.0));
        user.setAccounts(accounts);
        userDetailsService = new CustomUserDetailsService(user);
    }

    @Test
    public void testGetPassword() {
        assertEquals("password", userDetailsService.getPassword());
    }

    @Test
    public void testGetUsername() {
        assertEquals("john@example.com", userDetailsService.getUsername());
    }

    @Test
    public void testAccountNonExpired() {
        assertTrue(userDetailsService.isAccountNonExpired());
    }

    @Test
    public void testAccountNonLocked() {
        assertTrue(userDetailsService.isAccountNonLocked());
    }

    @Test
    public void testCredentialsNonExpired() {
        assertTrue(userDetailsService.isCredentialsNonExpired());
    }

    @Test
    public void testIsEnabled() {
        assertTrue(userDetailsService.isEnabled());
    }

    @Test
    public void testGetAuthorities() {
        assertNull(userDetailsService.getAuthorities());
    }


}
