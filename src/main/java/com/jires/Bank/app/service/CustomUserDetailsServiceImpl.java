package com.jires.Bank.app.service;

import com.jires.Bank.app.domain.ConfirmationToken;
import com.jires.Bank.app.domain.EmailSender;
import com.jires.Bank.app.domain.User;
import com.jires.Bank.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final EmailSender emailSender;
    private final ConfirmationTokenService confirmationTokenService;

    // Constructor injection of EmailSender and ConfirmationTokenService
    public CustomUserDetailsServiceImpl(EmailSender emailSender, ConfirmationTokenService confirmationTokenService) {
        this.emailSender = emailSender;
        this.confirmationTokenService = confirmationTokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Find the user with the given email address
        User user = UserRepository.findUserEmail(email);

        // If no user was found, throw an exception
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Generate a random token
        String token = UUID.randomUUID().toString();

        // Create a new confirmation token and save it to the database
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                false,
                user.getId()
        );
        ConfirmationTokenService.saveConfirmationToken(confirmationToken);

        // Send an email to the user containing a link to confirm their account
        String address = user.getEmail();
        String content = "Prosím potvrďte přihlášení ná váš účet kliknutím na link níže / Please click the link below to verify your account: \n\n" + "<a href='https://bank-application-production.herokuapp.com/confirm?token=" + token + "'>https://bank-application-production.herokuapp.com/confirm?token=" + token + "</a>";
        emailSender.send(address,content);
        System.out.println("localhost:8080/confirm?token=" + token);

        // Wait for the user to confirm their account by checking the confirmation token
        Optional<ConfirmationToken> optionalToken = confirmationTokenService.getToken(token);
        while (!optionalToken.isPresent() || !optionalToken.get().getConfirmed()) {
            optionalToken = confirmationTokenService.getToken(token);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Return a UserDetails object for the authenticated user
        return new CustomUserDetailsService(user);
    }

    public String confirmToken(String token) {

        // Get the confirmation token from the database
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found"));

        // If the token has already been confirmed, throw an exception
        if (confirmationToken.getConfirmed()) {
            throw new IllegalStateException("Email already confirmed");
        }

        // If the token has expired, throw an exception
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired");
        }

        // Set the confirmation date for the token
        confirmationTokenService.setConfirmedAt(token);

        // Return a confirmation message
        return "Confirmed";
    }
}