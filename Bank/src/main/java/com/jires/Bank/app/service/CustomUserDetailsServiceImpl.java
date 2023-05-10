package com.jires.Bank.app.service;

import com.jires.Bank.app.domain.ConfirmationToken;
import com.jires.Bank.app.domain.User;
import com.jires.Bank.app.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;



@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = UserRepository.findUserEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),LocalDateTime.now().plusMinutes(15),user.getId());
        ConfirmationTokenService.saveConfirmationToken(confirmationToken);
        System.out.println(token);
        return new CustomUserDetailsService(user);
    }
}