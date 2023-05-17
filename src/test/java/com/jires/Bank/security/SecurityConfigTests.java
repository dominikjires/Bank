package com.jires.Bank.security;

import com.jires.Bank.app.domain.EmailSender;
import com.jires.Bank.app.security.SecurityConfig;
import com.jires.Bank.app.service.ConfirmationTokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurityConfigTests {
    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        EmailSender emailSenderMock = Mockito.mock(EmailSender.class);
        ConfirmationTokenService confirmationTokenServiceMock = Mockito.mock(ConfirmationTokenService.class);
        securityConfig = new SecurityConfig(emailSenderMock, confirmationTokenServiceMock);
    }

    @Test
    void userDetailsService_ReturnsCustomUserDetailsServiceImpl() {
        UserDetailsService userDetailsService = securityConfig.userDetailsService();
        Assertions.assertNotNull(userDetailsService);
        Assertions.assertTrue(true);
    }

    @Test
    void passwordEncoder_ReturnsBCryptPasswordEncoder() {
        BCryptPasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        Assertions.assertNotNull(passwordEncoder);
        Assertions.assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    void authenticationProvider_ReturnsDaoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = securityConfig.authenticationProvider();
        Assertions.assertNotNull(authenticationProvider);
        Assertions.assertTrue(authenticationProvider instanceof DaoAuthenticationProvider);
    }


}
