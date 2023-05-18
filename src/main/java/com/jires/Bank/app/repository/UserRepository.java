package com.jires.Bank.app.repository;

import com.jires.Bank.app.domain.User;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class UserRepository {

    // Finds a user with the given ID and returns it, or null if not found.
    public static User findUser(long id) {
        try (BufferedReader reader = new BufferedReader(new FileReader("data/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (id == Long.parseLong(parts[0])) {
                    User user = new User(Long.parseLong(parts[0]), parts[1], parts[2], parts[3], parts[4]);
                    return user;
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading user from file: " + e.getMessage());
        }
        return null;
    }

    // Finds the ID of a user with the given email and returns it, or 0 if not found.
    public static long getId(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader("data/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (Objects.equals(email, parts[3])) {
                    return Long.parseLong(parts[0]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading user from file: " + e.getMessage());
        }
        return 0;
    }

    // Finds a user with the given email and returns it, or null if not found.
    public static User findUserEmail(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader("data/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (Objects.equals(email, parts[3])) {
                    User user = new User(Long.parseLong(parts[0]), parts[1], parts[2], parts[3], parts[4]);
                    return user;
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading user from file: " + e.getMessage());
        }
        return null;
    }

    // Main method gor testing
    //public static void main(String[] args) {
    //    // This main method is used for testing. It finds the ID of a user with the email "jane.doe@example.com" and prints it to the console.
    //    System.out.println(getId("jane.doe@example.com"));
    //}
}