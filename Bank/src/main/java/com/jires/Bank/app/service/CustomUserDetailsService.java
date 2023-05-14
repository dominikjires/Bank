package com.jires.Bank.app.service;

import com.jires.Bank.app.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetailsService implements UserDetails {

    private User user;

    // Constructor that takes in a User object
    public CustomUserDetailsService(User user) {
        this.user = user;
    }

    // Returns the authorities granted to the user
    // Not implemented in this example
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    // Returns the user's password
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // Returns the user's email address as the username
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // Indicates whether the user's account has expired
    // In this example, always returns true
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Indicates whether the user is locked or not
    // In this example, always returns true
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Indicates whether the user's credentials (password) has expired
    // In this example, always returns true
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Indicates whether the user is enabled or disabled
    // In this example, always returns true
    @Override
    public boolean isEnabled() {
        return true;
    }

}
