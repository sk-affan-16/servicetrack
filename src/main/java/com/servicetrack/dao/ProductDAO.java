package com.servicetrack.dao;

import com.servicetrack.model.Product;
import com.servicetrack.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

public class ProductDAO {

    private static final String FIND_BY_ID_SQL =
            "SELECT product_id, customer_id, product_name, brand, " +
                    "model_number, serial_number, purchase_date, " +
                    "created_at, updated_at " +
                    "FROM products " +
                    "WHERE product_id = ?";

    private static final String FIND_BY_CUSTOMER_ID_SQL =
            "SELECT product_id, customer_id, product_name, brand, " +
                    "model_number, serial_number, purchase_date, " +
                    "created_at, updated_at " +
                    "FROM products " +
                    "WHERE customer_id = ? " +
                    "ORDER BY product_id DESC";

    private static final String FIND_BY_SERIAL_NUMBER_SQL =
            "SELECT product_id, customer_id, product_name, brand, " +
                    "model_number, serial_number, purchase_date, " +
                    "created_at, updated_at " +
                    "FROM products " +
                    "WHERE serial_number = ?";

    private static final String INSERT_PRODUCT_SQL =
            "INSERT INTO products " +
                    "(customer_id, product_name, brand, model_number, " +
                    "serial_number, purchase_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_PRODUCT_SQL =
            "UPDATE products " +
                    "SET product_name = ?, brand = ?, model_number = ?, " +
                    "serial_number = ?, purchase_date = ? " +
                    "WHERE product_id = ? AND customer_id = ?";

    private static final String DELETE_PRODUCT_SQL =
            "DELETE FROM products " +
                    "WHERE product_id = ? AND customer_id = ?";

    public Product findById(Long productId) throws SQLException {

        if (productId == null) {
            throw new IllegalArgumentException(
                    "Product ID is required."
            );
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setLong(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapProduct(resultSet);
                }

                return null;
            }
        }
    }

    public java.util.List<Product> findByCustomerId(
            Long customerId) throws SQLException {

        if (customerId == null) {
            throw new IllegalArgumentException(
                    "Customer ID is required."
            );
        }

        java.util.List<Product> products =
                new java.util.ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             FIND_BY_CUSTOMER_ID_SQL)) {

            statement.setLong(1, customerId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    products.add(mapProduct(resultSet));
                }
            }
        }

        return products;
    }

    public Product findBySerialNumber(
            String serialNumber) throws SQLException {

        if (serialNumber == null || serialNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "Serial number is required."
            );
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             FIND_BY_SERIAL_NUMBER_SQL)) {

            statement.setString(1, serialNumber.trim());

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapProduct(resultSet);
                }

                return null;
            }
        }
    }

    public Product create(Product product) throws SQLException {

        if (product == null) {
            throw new IllegalArgumentException(
                    "Product is required."
            );
        }

        if (product.getCustomerId() == null) {
            throw new IllegalArgumentException(
                    "Customer ID is required."
            );
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             INSERT_PRODUCT_SQL,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(
                    1,
                    product.getCustomerId()
            );

            statement.setString(
                    2,
                    product.getProductName()
            );

            statement.setString(
                    3,
                    product.getBrand()
            );

            statement.setString(
                    4,
                    product.getModelNumber()
            );

            statement.setString(
                    5,
                    product.getSerialNumber()
            );

            statement.setDate(
                    6,
                    Date.valueOf(product.getPurchaseDate())
            );

            int affectedRows =
                    statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException(
                        "Creating product failed."
                );
            }

            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {

                    product.setProductId(
                            generatedKeys.getLong(1)
                    );

                } else {

                    throw new SQLException(
                            "Creating product failed: "
                                    + "no ID obtained."
                    );
                }
            }

            return product;
        }
    }

    public boolean update(Product product)
            throws SQLException {

        if (product == null) {
            throw new IllegalArgumentException(
                    "Product is required."
            );
        }

        if (product.getProductId() == null) {
            throw new IllegalArgumentException(
                    "Product ID is required."
            );
        }

        if (product.getCustomerId() == null) {
            throw new IllegalArgumentException(
                    "Customer ID is required."
            );
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             UPDATE_PRODUCT_SQL)) {

            statement.setString(
                    1,
                    product.getProductName()
            );

            statement.setString(
                    2,
                    product.getBrand()
            );

            statement.setString(
                    3,
                    product.getModelNumber()
            );

            statement.setString(
                    4,
                    product.getSerialNumber()
            );

            statement.setDate(
                    5,
                    Date.valueOf(product.getPurchaseDate())
            );

            statement.setLong(
                    6,
                    product.getProductId()
            );

            statement.setLong(
                    7,
                    product.getCustomerId()
            );

            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete(Long productId, Long customerId)
            throws SQLException {

        if (productId == null) {
            throw new IllegalArgumentException(
                    "Product ID is required."
            );
        }

        if (customerId == null) {
            throw new IllegalArgumentException(
                    "Customer ID is required."
            );
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             DELETE_PRODUCT_SQL)) {

            statement.setLong(1, productId);
            statement.setLong(2, customerId);

            return statement.executeUpdate() > 0;
        }
    }

    private Product mapProduct(ResultSet resultSet)
            throws SQLException {

        Product product = new Product();

        product.setProductId(
                resultSet.getLong("product_id")
        );

        product.setCustomerId(
                resultSet.getLong("customer_id")
        );

        product.setProductName(
                resultSet.getString("product_name")
        );

        product.setBrand(
                resultSet.getString("brand")
        );

        product.setModelNumber(
                resultSet.getString("model_number")
        );

        product.setSerialNumber(
                resultSet.getString("serial_number")
        );

        Date purchaseDate =
                resultSet.getDate("purchase_date");

        if (purchaseDate != null) {
            product.setPurchaseDate(
                    purchaseDate.toLocalDate()
            );
        }

        if (resultSet.getTimestamp("created_at") != null) {
            product.setCreatedAt(
                    resultSet.getTimestamp("created_at")
                            .toLocalDateTime()
            );
        }

        if (resultSet.getTimestamp("updated_at") != null) {
            product.setUpdatedAt(
                    resultSet.getTimestamp("updated_at")
                            .toLocalDateTime()
            );
        }

        return product;
    }
}