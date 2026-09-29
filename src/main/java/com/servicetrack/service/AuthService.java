package com.servicetrack.service;

import com.servicetrack.dao.CustomerDAO;
import com.servicetrack.dao.UserDAO;
import com.servicetrack.model.Customer;
import com.servicetrack.model.User;
import com.servicetrack.util.PasswordUtil;

import java.sql.SQLException;

public class AuthService {

    private final UserDAO userDAO;
    private final CustomerDAO customerDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
        this.customerDAO = new CustomerDAO();
    }

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

        fullName = fullName.trim();
        email = email.trim().toLowerCase();
        username = username.trim();

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

        String passwordHash =
                PasswordUtil.hashPassword(password);

        User user = new User();

        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(
                phone == null || phone.isBlank()
                        ? null
                        : phone.trim()
        );
        user.setUsername(username);
        user.setPasswordHash(passwordHash);

        user.setRole("CUSTOMER");
        user.setStatus("ACTIVE");

        User createdUser =
                userDAO.create(user);

        Customer customer = new Customer();

        customer.setUserId(
                createdUser.getUserId()
        );

        customer.setAddress(null);
        customer.setCity(null);
        customer.setState(null);
        customer.setPincode(null);

        customerDAO.create(customer);

        return createdUser;
    }

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

        User user =
                userDAO.findByUsername(
                        username.trim()
                );

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

    public User getUserById(Long userId)
            throws SQLException {

        if (userId == null) {
            throw new IllegalArgumentException(
                    "User ID is required."
            );
        }

        User user =
                userDAO.findById(userId);

        if (user == null) {
            throw new IllegalArgumentException(
                    "User profile not found."
            );
        }

        return user;
    }

    public User updateProfile(
            Long userId,
            String fullName,
            String email,
            String phone) throws SQLException {

        if (userId == null) {
            throw new IllegalArgumentException(
                    "User ID is required."
            );
        }

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

        fullName = fullName.trim();
        email = email.trim().toLowerCase();

        if (fullName.length() > 100) {
            throw new IllegalArgumentException(
                    "Full name is too long."
            );
        }

        if (email.length() > 150) {
            throw new IllegalArgumentException(
                    "Email is too long."
            );
        }

        if (!email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

            throw new IllegalArgumentException(
                    "Invalid email address."
            );
        }

        if (phone != null) {

            phone = phone.trim();

            if (phone.isBlank()) {
                phone = null;
            }

            if (phone != null && phone.length() > 20) {
                throw new IllegalArgumentException(
                        "Phone number is too long."
                );
            }
        }

        User existingUser =
                userDAO.findByEmail(email);

        if (existingUser != null
                && !existingUser.getUserId().equals(userId)) {

            throw new IllegalArgumentException(
                    "Email already exists."
            );
        }

        boolean updated =
                userDAO.updateProfile(
                        userId,
                        fullName,
                        email,
                        phone
                );

        if (!updated) {
            throw new IllegalArgumentException(
                    "Profile update failed."
            );
        }

        User updatedUser =
                userDAO.findById(userId);

        if (updatedUser == null) {
            throw new IllegalArgumentException(
                    "Updated user could not be loaded."
            );
        }

        return updatedUser;
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