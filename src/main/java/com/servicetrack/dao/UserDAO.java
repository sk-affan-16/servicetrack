package com.servicetrack.dao;

import com.servicetrack.model.User;
import com.servicetrack.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {

    private static final String INSERT_USER_SQL =
            "INSERT INTO users " +
                    "(full_name, email, phone, username, password_hash, role, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String FIND_BY_USERNAME_SQL =
            "SELECT user_id, full_name, email, phone, username, " +
                    "password_hash, role, status, created_at, updated_at " +
                    "FROM users " +
                    "WHERE username = ?";

    private static final String FIND_BY_EMAIL_SQL =
            "SELECT user_id, full_name, email, phone, username, " +
                    "password_hash, role, status, created_at, updated_at " +
                    "FROM users " +
                    "WHERE email = ?";

    public User findByUsername(String username) throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_USERNAME_SQL)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapUser(resultSet);
                }

                return null;
            }
        }
    }

    public User findByEmail(String email) throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_EMAIL_SQL)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapUser(resultSet);
                }

                return null;
            }
        }
    }

    public User create(User user) throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             INSERT_USER_SQL,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getFullName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPhone());
            statement.setString(4, user.getUsername());
            statement.setString(5, user.getPasswordHash());
            statement.setString(6, user.getRole());
            statement.setString(7, user.getStatus());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed.");
            }

            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {
                    user.setUserId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException(
                            "Creating user failed: no ID obtained.");
                }
            }

            return user;
        }
    }

    private User mapUser(ResultSet resultSet) throws SQLException {

        User user = new User();

        user.setUserId(resultSet.getLong("user_id"));
        user.setFullName(resultSet.getString("full_name"));
        user.setEmail(resultSet.getString("email"));
        user.setPhone(resultSet.getString("phone"));
        user.setUsername(resultSet.getString("username"));
        user.setPasswordHash(resultSet.getString("password_hash"));
        user.setRole(resultSet.getString("role"));
        user.setStatus(resultSet.getString("status"));

        if (resultSet.getTimestamp("created_at") != null) {
            user.setCreatedAt(
                    resultSet.getTimestamp("created_at")
                            .toLocalDateTime()
            );
        }

        if (resultSet.getTimestamp("updated_at") != null) {
            user.setUpdatedAt(
                    resultSet.getTimestamp("updated_at")
                            .toLocalDateTime()
            );
        }

        return user;
    }
}