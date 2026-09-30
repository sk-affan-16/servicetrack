package com.servicetrack.dao;

import com.servicetrack.model.Warranty;
import com.servicetrack.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class WarrantyDAO {

    private static final String FIND_BY_ID_SQL =
            "SELECT warranty_id, product_id, warranty_start, " +
                    "warranty_end, warranty_status, created_at, updated_at " +
                    "FROM warranties " +
                    "WHERE warranty_id = ?";

    private static final String FIND_BY_PRODUCT_ID_SQL =
            "SELECT warranty_id, product_id, warranty_start, " +
                    "warranty_end, warranty_status, created_at, updated_at " +
                    "FROM warranties " +
                    "WHERE product_id = ?";

    private static final String INSERT_WARRANTY_SQL =
            "INSERT INTO warranties " +
                    "(product_id, warranty_start, warranty_end, warranty_status) " +
                    "VALUES (?, ?, ?, ?)";

    private static final String UPDATE_STATUS_SQL =
            "UPDATE warranties " +
                    "SET warranty_status = ? " +
                    "WHERE warranty_id = ?";

    public Warranty findById(Long warrantyId)
            throws SQLException {

        if (warrantyId == null) {
            throw new IllegalArgumentException(
                    "Warranty ID is required."
            );
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setLong(1, warrantyId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapWarranty(resultSet);
                }

                return null;
            }
        }
    }

    public Warranty findByProductId(Long productId)
            throws SQLException {

        if (productId == null) {
            throw new IllegalArgumentException(
                    "Product ID is required."
            );
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             FIND_BY_PRODUCT_ID_SQL)) {

            statement.setLong(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapWarranty(resultSet);
                }

                return null;
            }
        }
    }

    public Warranty create(Warranty warranty)
            throws SQLException {

        if (warranty == null) {
            throw new IllegalArgumentException(
                    "Warranty is required."
            );
        }

        if (warranty.getProductId() == null) {
            throw new IllegalArgumentException(
                    "Product ID is required."
            );
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             INSERT_WARRANTY_SQL,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(
                    1,
                    warranty.getProductId()
            );

            statement.setDate(
                    2,
                    Date.valueOf(warranty.getWarrantyStart())
            );

            statement.setDate(
                    3,
                    Date.valueOf(warranty.getWarrantyEnd())
            );

            statement.setString(
                    4,
                    warranty.getWarrantyStatus()
            );

            int affectedRows =
                    statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException(
                        "Creating warranty failed."
                );
            }

            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {

                    warranty.setWarrantyId(
                            generatedKeys.getLong(1)
                    );

                } else {

                    throw new SQLException(
                            "Creating warranty failed: "
                                    + "no ID obtained."
                    );
                }
            }

            return warranty;
        }
    }

    public boolean updateStatus(
            Long warrantyId,
            String warrantyStatus)
            throws SQLException {

        if (warrantyId == null) {
            throw new IllegalArgumentException(
                    "Warranty ID is required."
            );
        }

        if (warrantyStatus == null
                || warrantyStatus.isBlank()) {

            throw new IllegalArgumentException(
                    "Warranty status is required."
            );
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             UPDATE_STATUS_SQL)) {

            statement.setString(
                    1,
                    warrantyStatus
            );

            statement.setLong(
                    2,
                    warrantyId
            );

            return statement.executeUpdate() > 0;
        }
    }

    private Warranty mapWarranty(
            ResultSet resultSet)
            throws SQLException {

        Warranty warranty = new Warranty();

        warranty.setWarrantyId(
                resultSet.getLong("warranty_id")
        );

        warranty.setProductId(
                resultSet.getLong("product_id")
        );

        Date warrantyStart =
                resultSet.getDate("warranty_start");

        if (warrantyStart != null) {
            warranty.setWarrantyStart(
                    warrantyStart.toLocalDate()
            );
        }

        Date warrantyEnd =
                resultSet.getDate("warranty_end");

        if (warrantyEnd != null) {
            warranty.setWarrantyEnd(
                    warrantyEnd.toLocalDate()
            );
        }

        warranty.setWarrantyStatus(
                resultSet.getString("warranty_status")
        );

        if (resultSet.getTimestamp("created_at") != null) {
            warranty.setCreatedAt(
                    resultSet.getTimestamp("created_at")
                            .toLocalDateTime()
            );
        }

        if (resultSet.getTimestamp("updated_at") != null) {
            warranty.setUpdatedAt(
                    resultSet.getTimestamp("updated_at")
                            .toLocalDateTime()
            );
        }

        return warranty;
    }
}