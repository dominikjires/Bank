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


    public CustomUserDetailsServiceImpl(EmailSender emailSender, ConfirmationTokenService confirmationTokenService) {
        this.emailSender = emailSender;
        this.confirmationTokenService = confirmationTokenService;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = UserRepository.findUserEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),LocalDateTime.now().plusMinutes(15),false,user.getId());
        ConfirmationTokenService.saveConfirmationToken(confirmationToken);
        System.out.println(token);
        String address = user.getEmail();
        String content = "Prosím potvrďte přihlášení ná váš účet kliknutím na link níže / Please click the link below to verify your account: \n\n" + "http://localhost:8080/confirm?token=" + token;
        System.out.println(content);
        emailSender.send(address,content);
        Optional<ConfirmationToken> optionalToken = confirmationTokenService.getToken(token);
        while (!optionalToken.isPresent() || !optionalToken.get().getConfirmed()) {
            optionalToken = confirmationTokenService.getToken(token);
            try {
                Thread.sleep(2000); // počkej 2 sekundy
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return new CustomUserDetailsService(user);
    }

    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElseThrow(() -> new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmed()) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }
        confirmationTokenService.setConfirmedAt(token);
        return "Potvrzeno / confirmed";
    }
}