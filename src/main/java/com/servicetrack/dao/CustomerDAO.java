package com.servicetrack.dao;

import com.servicetrack.model.Customer;
import com.servicetrack.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {

    private static final String FIND_BY_USER_ID_SQL =
            "SELECT customer_id, user_id, address, city, state, " +
                    "pincode, created_at, updated_at " +
                    "FROM customers " +
                    "WHERE user_id = ?";

    private static final String INSERT_CUSTOMER_SQL =
            "INSERT INTO customers " +
                    "(user_id, address, city, state, pincode) " +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_CUSTOMER_SQL =
            "UPDATE customers " +
                    "SET address = ?, city = ?, state = ?, pincode = ? " +
                    "WHERE user_id = ?";

    public Customer findByUserId(Long userId)
            throws SQLException {

        if (userId == null) {
            throw new IllegalArgumentException(
                    "User ID is required."
            );
        }

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             FIND_BY_USER_ID_SQL)) {

            statement.setLong(1, userId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapCustomer(resultSet);
                }

                return null;
            }
        }
    }

    public Customer create(Customer customer)
            throws SQLException {

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer is required."
            );
        }

        if (customer.getUserId() == null) {
            throw new IllegalArgumentException(
                    "User ID is required."
            );
        }

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             INSERT_CUSTOMER_SQL,
                             java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(
                    1,
                    customer.getUserId()
            );

            statement.setString(
                    2,
                    customer.getAddress()
            );

            statement.setString(
                    3,
                    customer.getCity()
            );

            statement.setString(
                    4,
                    customer.getState()
            );

            statement.setString(
                    5,
                    customer.getPincode()
            );

            int affectedRows =
                    statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException(
                        "Creating customer failed."
                );
            }

            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {

                    customer.setCustomerId(
                            generatedKeys.getLong(1)
                    );

                } else {

                    throw new SQLException(
                            "Creating customer failed: "
                                    + "no ID obtained."
                    );
                }
            }

            return customer;
        }
    }

    public boolean update(Customer customer)
            throws SQLException {

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer is required."
            );
        }

        if (customer.getUserId() == null) {
            throw new IllegalArgumentException(
                    "User ID is required."
            );
        }

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             UPDATE_CUSTOMER_SQL)) {

            statement.setString(
                    1,
                    customer.getAddress()
            );

            statement.setString(
                    2,
                    customer.getCity()
            );

            statement.setString(
                    3,
                    customer.getState()
            );

            statement.setString(
                    4,
                    customer.getPincode()
            );

            statement.setLong(
                    5,
                    customer.getUserId()
            );

            return statement.executeUpdate() > 0;
        }
    }

    private Customer mapCustomer(
            ResultSet resultSet)
            throws SQLException {

        Customer customer = new Customer();

        customer.setCustomerId(
                resultSet.getLong("customer_id")
        );

        customer.setUserId(
                resultSet.getLong("user_id")
        );

        customer.setAddress(
                resultSet.getString("address")
        );

        customer.setCity(
                resultSet.getString("city")
        );

        customer.setState(
                resultSet.getString("state")
        );

        customer.setPincode(
                resultSet.getString("pincode")
        );

        if (resultSet.getTimestamp("created_at") != null) {

            customer.setCreatedAt(
                    resultSet.getTimestamp("created_at")
                            .toLocalDateTime()
            );
        }

        if (resultSet.getTimestamp("updated_at") != null) {

            customer.setUpdatedAt(
                    resultSet.getTimestamp("updated_at")
                            .toLocalDateTime()
            );
        }

        return customer;
    }
}