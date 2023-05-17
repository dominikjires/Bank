package com.jires.Bank.repository;

import com.jires.Bank.app.domain.User;
import com.jires.Bank.app.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTests {
    @Test
    public void testFindUser_UserExists() {
        long id = 1;
        User user = UserRepository.findUser(id);
        assertNotNull(user);
        assertEquals(id, user.getId());
    }

    @Test
    public void testFindUser_UserNotExists() {
        long id = 100;
        User user = UserRepository.findUser(id);
        assertNull(user);
    }

    @Test
    public void testGetId_UserExists() {
        String email = "jane.doe@example.com";
        long id = UserRepository.getId(email);
        assertNotEquals(0, id);
    }

    @Test
    public void testGetId_UserNotExists() {
        String email = "nonexistent@example.com";
        long id = UserRepository.getId(email);
        assertEquals(0, id);
    }

    @Test
    public void testFindUserEmail_UserExists() {
        String email = "jane.doe@example.com";
        User user = UserRepository.findUserEmail(email);
        assertNotNull(user);
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testFindUserEmail_UserNotExists() {
        String email = "nonexistent@example.com";
        User user = UserRepository.findUserEmail(email);
        assertNull(user);
    }


}
