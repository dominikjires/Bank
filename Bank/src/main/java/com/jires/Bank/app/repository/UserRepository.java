package com.jires.Bank.app.repository;

import com.jires.Bank.app.domain.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserRepository {

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

    public static void main(String[] args) {
        //List<User> users = new ArrayList<>();
        //users = findUsers();
        //System.out.println(users);
        //System.out.println(findUser(1));
        System.out.println(getId("jane.doe@example.com"));
    }

}
