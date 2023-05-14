package com.jires.Bank.app.security;

import com.jires.Bank.app.domain.EmailSender;
import com.jires.Bank.app.service.ConfirmationTokenService;
import com.jires.Bank.app.service.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Autowired dependencies for email sending and token confirmation service
    @Autowired
    private final EmailSender emailSender;
    private final ConfirmationTokenService confirmationTokenService;

    // Constructor to set autowired dependencies
    public SecurityConfig(EmailSender emailSender, ConfirmationTokenService confirmationTokenService) {
        this.emailSender = emailSender;
        this.confirmationTokenService = confirmationTokenService;
    }

    // Bean to return custom user details service
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsServiceImpl(emailSender, confirmationTokenService);
    }

    // Bean to return BCrypt password encoder
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean to return DAO authentication provider
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Bean to configure HTTP security and set login and logout URLs and parameters
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers("/dashboard")
                .authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .defaultSuccessUrl("/dashboard")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/error.html");

        return http.build();
    }
}