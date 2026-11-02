package com.servicetrack.util;

import com.servicetrack.dao.UserDAO;
import com.servicetrack.model.User;

import java.io.Console;
import java.util.Arrays;

public class AdminBootstrap {

    public static void main(String[] args) throws Exception {

        Console console = System.console();

        if (console == null) {
            System.out.println("Run this program directly from Terminal.");
            return;
        }

        UserDAO userDAO = new UserDAO();

        System.out.println("=== ServiceTrack Admin Creation ===");

        System.out.print("Full Name: ");
        String fullName = console.readLine().trim();

        System.out.print("Email: ");
        String email = console.readLine().trim().toLowerCase();

        System.out.print("Username: ");
        String username = console.readLine().trim();

        System.out.print("Phone (optional): ");
        String phone = console.readLine().trim();

        if (userDAO.findByUsername(username) != null
                || userDAO.findByEmail(email) != null) {
            System.out.println("Username or email already exists.");
            return;
        }

        char[] password = console.readPassword("Password: ");
        char[] confirm = console.readPassword("Confirm password: ");

        try {
            if (password.length < 12) {
                throw new IllegalArgumentException(
                        "Password must have at least 12 characters."
                );
            }

            if (!Arrays.equals(password, confirm)) {
                throw new IllegalArgumentException(
                        "Passwords do not match."
                );
            }

            User admin = new User();

            admin.setFullName(fullName);
            admin.setEmail(email);
            admin.setPhone(phone.isBlank() ? null : phone);
            admin.setUsername(username);
            admin.setPasswordHash(
                    PasswordUtil.hashPassword(new String(password))
            );
            admin.setRole("ADMIN");
            admin.setStatus("ACTIVE");

            userDAO.create(admin);

            System.out.println("\nAdmin account created successfully!");
            System.out.println("Username: " + username);
            System.out.println("Role: ADMIN");

        } finally {
            Arrays.fill(password, '\0');
            Arrays.fill(confirm, '\0');
        }
    }
}