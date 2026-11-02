package com.servicetrack.service;

import com.servicetrack.dao.UserDAO;
import com.servicetrack.model.User;
import com.servicetrack.util.PasswordUtil;

import java.sql.SQLException;
import java.util.List;

public class TechnicianManagementService {
    private final UserDAO userDAO = new UserDAO();

    public List<User> getTechnicians() throws SQLException {
        return userDAO.findByRole("TECHNICIAN");
    }

    public User createTechnician(String fullName, String email, String phone,
                                 String username, String password) throws SQLException {
        if (fullName == null || fullName.isBlank()
                || email == null || email.isBlank()
                || username == null || username.isBlank()
                || password == null || password.length() < 8) {
            throw new IllegalArgumentException("Name, email, username and a password of at least 8 characters are required.");
        }
        fullName = fullName.trim();
        email = email.trim().toLowerCase();
        username = username.trim();
        phone = phone == null || phone.isBlank() ? null : phone.trim();

        if (fullName.length() > 100 || email.length() > 150
                || username.length() > 50 || (phone != null && phone.length() > 20)
                || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Please check the length and format of the supplied details.");
        }
        if (userDAO.findByUsername(username) != null) {
            throw new IllegalArgumentException("Username already exists.");
        }
        if (userDAO.findByEmail(email) != null) {
            throw new IllegalArgumentException("Email already exists.");
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setUsername(username);
        user.setPasswordHash(PasswordUtil.hashPassword(password));
        user.setRole("TECHNICIAN");
        user.setStatus("ACTIVE");
        return userDAO.create(user);
    }

    public void changeStatus(Long userId, String status) throws SQLException {
        if (userId == null || userId <= 0
                || !("ACTIVE".equals(status) || "INACTIVE".equals(status))) {
            throw new IllegalArgumentException("Invalid technician or status.");
        }
        if (!userDAO.updateStatus(userId, status)) {
            throw new IllegalArgumentException("Technician not found.");
        }
    }
}
