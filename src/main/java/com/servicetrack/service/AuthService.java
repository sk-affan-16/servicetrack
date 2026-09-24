package com.servicetrack.service;

import com.servicetrack.dao.UserDAO;
import com.servicetrack.model.User;
import com.servicetrack.util.PasswordUtil;

import java.sql.SQLException;

public class AuthService {

    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Registers a new CUSTOMER account.
     *
     * Public registration must never allow the client
     * to choose ADMIN or TECHNICIAN as the role.
     */
    public User registerCustomer(
            String fullName,
            String email,
            String phone,
            String username,
            String password) throws SQLException {

        validateRegistrationInput(
                fullName,
                email,
                username,
                password
        );

        if (userDAO.findByUsername(username) != null) {
            throw new IllegalArgumentException(
                    "Username already exists."
            );
        }

        if (userDAO.findByEmail(email) != null) {
            throw new IllegalArgumentException(
                    "Email already exists."
            );
        }

        String passwordHash = PasswordUtil.hashPassword(password);

        User user = new User();

        user.setFullName(fullName.trim());
        user.setEmail(email.trim().toLowerCase());
        user.setPhone(
                phone == null || phone.isBlank()
                        ? null
                        : phone.trim()
        );
        user.setUsername(username.trim());
        user.setPasswordHash(passwordHash);

        // Public registration always creates CUSTOMER.
        user.setRole("CUSTOMER");
        user.setStatus("ACTIVE");

        return userDAO.create(user);
    }

    /**
     * Authenticates a user using username and password.
     */
    public User login(
            String username,
            String password) throws SQLException {

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException(
                    "Username is required."
            );
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException(
                    "Password is required."
            );
        }

        User user = userDAO.findByUsername(username.trim());

        if (user == null) {
            return null;
        }

        if (!"ACTIVE".equals(user.getStatus())) {
            return null;
        }

        boolean passwordValid =
                PasswordUtil.verifyPassword(
                        password,
                        user.getPasswordHash()
                );

        if (!passwordValid) {
            return null;
        }

        return user;
    }

    private void validateRegistrationInput(
            String fullName,
            String email,
            String username,
            String password) {

        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException(
                    "Full name is required."
            );
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "Email is required."
            );
        }

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException(
                    "Username is required."
            );
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException(
                    "Password is required."
            );
        }

        if (fullName.trim().length() > 100) {
            throw new IllegalArgumentException(
                    "Full name is too long."
            );
        }

        if (email.trim().length() > 150) {
            throw new IllegalArgumentException(
                    "Email is too long."
            );
        }

        if (username.trim().length() > 50) {
            throw new IllegalArgumentException(
                    "Username is too long."
            );
        }

        if (password.length() < 8) {
            throw new IllegalArgumentException(
                    "Password must contain at least 8 characters."
            );
        }

        if (!email.trim().matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

            throw new IllegalArgumentException(
                    "Invalid email address."
            );
        }
    }
}