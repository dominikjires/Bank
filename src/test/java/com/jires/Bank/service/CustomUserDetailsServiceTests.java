package com.jires.Bank.service;

import com.jires.Bank.app.domain.ConfirmationToken;
import com.jires.Bank.app.domain.EmailSender;
import com.jires.Bank.app.domain.User;
import com.jires.Bank.app.repository.UserRepository;
import com.jires.Bank.app.service.ConfirmationTokenService;
import com.jires.Bank.app.service.CustomUserDetailsService;
import com.jires.Bank.app.service.CustomUserDetailsServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.swing.table.TableCellEditor;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomUserDetailsServiceTests {
    @Mock
    private EmailSender emailSender;

    @Mock
    private ConfirmationTokenService confirmationTokenService;

    private CustomUserDetailsServiceImpl customUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customUserDetailsService = new CustomUserDetailsServiceImpl(emailSender, confirmationTokenService);
    }

    @Test
    void loadUserByUsername_UserFound_ReturnsUserDetails() {
        //Test ceka na potvrzeni od uzivatele, neni mozno provadet
        //String email = "abc@def.cz";
        //User user = new User(10, "Jan", "Novy", email, "password");

        //UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        //assertNotNull(userDetails);
        //assertEquals(user.getEmail(), userDetails.getUsername());
        // Add more assertions for other properties if needed
    }

    @Test
    void confirmToken_ValidToken_TokenConfirmed() {
        // Arrange
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                false,
                10L);
        when(confirmationTokenService.getToken(token)).thenReturn(Optional.of(confirmationToken));

        String result = customUserDetailsService.confirmToken(token);

        assertEquals("Confirmed", result);
    }

    @Test
    void confirmToken_TokenNotFound_ThrowsIllegalStateException() {
        String token = UUID.randomUUID().toString();
        when(confirmationTokenService.getToken(token)).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> customUserDetailsService.confirmToken(token));
    }


}
