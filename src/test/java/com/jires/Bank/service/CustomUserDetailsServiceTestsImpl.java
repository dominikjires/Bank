package com.jires.Bank.service;

import com.jires.Bank.app.domain.ConfirmationToken;
import com.jires.Bank.app.domain.EmailSender;
import com.jires.Bank.app.domain.User;
import com.jires.Bank.app.repository.UserRepository;
import com.jires.Bank.app.service.ConfirmationTokenService;
import com.jires.Bank.app.service.CustomUserDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomUserDetailsServiceTestsImpl {
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
        // Nelze otestovat ceka se na potvrzeni od uzivatele
        //User user = new User(3, "Dominik", "Jires", "jiresdom@gmail.com", "password");

        //UserDetails userDetails = customUserDetailsService.loadUserByUsername("jiresdom@gmail.com");

        //assertNotNull(userDetails);
        //assertEquals(user.getEmail(), userDetails.getUsername());
    }

    @Test
    public void testConfirmToken() {
        EmailSender emailSender = mock(EmailSender.class);
        ConfirmationTokenService confirmationTokenService = mock(ConfirmationTokenService.class);
        CustomUserDetailsServiceImpl userService = new CustomUserDetailsServiceImpl(emailSender, confirmationTokenService);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                false,
                1L
        );

        when(confirmationTokenService.getToken(token)).thenReturn(Optional.of(confirmationToken));

        String result = userService.confirmToken(token);

        assertEquals("Confirmed", result);
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

    @Test
    void confirmToken_TokenAlreadyConfirmed_ThrowsIllegalStateException() {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                true,
                10L);
        when(confirmationTokenService.getToken(token)).thenReturn(Optional.of(confirmationToken));
        assertThrows(IllegalStateException.class, () -> customUserDetailsService.confirmToken(token));
    }

    @Test
    void confirmToken_TokenExpired_ThrowsIllegalStateException() {
        // Arrange
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token,
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().minusMinutes(1),
                false,
                10L);
        when(confirmationTokenService.getToken(token)).thenReturn(Optional.of(confirmationToken));

        assertThrows(IllegalStateException.class, () -> customUserDetailsService.confirmToken(token));
    }

    @Test
    public void testConfirmToken_TokenNotFound() {
        EmailSender emailSender = mock(EmailSender.class);
        ConfirmationTokenService confirmationTokenService = mock(ConfirmationTokenService.class);
        CustomUserDetailsServiceImpl userService = new CustomUserDetailsServiceImpl(emailSender, confirmationTokenService);

        String token = UUID.randomUUID().toString();
        when(confirmationTokenService.getToken(token)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> userService.confirmToken(token));
        verify(confirmationTokenService, never()).setConfirmedAt(token);
    }

    @Test
    public void testConfirmToken_TokenAlreadyConfirmed() {
        // Arrange
        EmailSender emailSender = mock(EmailSender.class);
        ConfirmationTokenService confirmationTokenService = mock(ConfirmationTokenService.class);
        CustomUserDetailsServiceImpl userService = new CustomUserDetailsServiceImpl(emailSender, confirmationTokenService);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                true,
                1L
        );

        when(confirmationTokenService.getToken(token)).thenReturn(Optional.of(confirmationToken));

        assertThrows(IllegalStateException.class, () -> userService.confirmToken(token));
        verify(confirmationTokenService, never()).setConfirmedAt(token);
    }

    @Test
    public void testConfirmToken_TokenExpired() {
        // Arrange
        EmailSender emailSender = mock(EmailSender.class);
        ConfirmationTokenService confirmationTokenService = mock(ConfirmationTokenService.class);
        CustomUserDetailsServiceImpl userService = new CustomUserDetailsServiceImpl(emailSender, confirmationTokenService);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now().minusMinutes(30),
                LocalDateTime.now().minusMinutes(15),
                false,
                1l
        );

        when(confirmationTokenService.getToken(token)).thenReturn(Optional.of(confirmationToken));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> userService.confirmToken(token));
        verify(confirmationTokenService, never()).setConfirmedAt(token);
    }

    @Test
    void confirmToken_ValidToken_ReturnsConfirmedMessage() {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                false,
                1L
        );
        when(confirmationTokenService.getToken(token)).thenReturn(Optional.of(confirmationToken));
        String result = customUserDetailsService.confirmToken(token);
        assertEquals("Confirmed", result);
        verify(confirmationTokenService).setConfirmedAt(token);
    }

    @Test
    void confirmToken_ExpiredToken_ThrowsIllegalStateException() {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now().minusMinutes(30), // Expired
                LocalDateTime.now().minusMinutes(15),
                false,
                1L
        );
        when(confirmationTokenService.getToken(token)).thenReturn(Optional.of(confirmationToken));

        assertThrows(IllegalStateException.class, () -> customUserDetailsService.confirmToken(token));
        assertFalse(confirmationToken.getConfirmed());
        verify(confirmationTokenService, never()).setConfirmedAt(token);
    }

    @Test
    void confirmToken_AlreadyConfirmedToken_ThrowsIllegalStateException() {
        // Arrange
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                true, // Already confirmed
                1L
        );
        when(confirmationTokenService.getToken(token)).thenReturn(Optional.of(confirmationToken));

    }

    @Test
    public void testLoadUserByUsername_InvalidUsername() {
        EmailSender emailSender = Mockito.mock(EmailSender.class);
        ConfirmationTokenService confirmationTokenService = Mockito.mock(ConfirmationTokenService.class);

        CustomUserDetailsServiceImpl userDetailsService = new CustomUserDetailsServiceImpl(emailSender, confirmationTokenService);

        // Try to load user with an invalid username
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("invalid@example.com"));
    }





    }
