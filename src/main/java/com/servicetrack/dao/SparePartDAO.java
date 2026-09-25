package com.servicetrack.dao;

import com.servicetrack.model.SparePart;
import com.servicetrack.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SparePartDAO {

    private static final String INSERT_SQL =
            "INSERT INTO spare_parts " +
                    "(part_name, part_number, quantity, unit_price) " +
                    "VALUES (?, ?, ?, ?)";

    private static final String FIND_BY_ID_SQL =
            "SELECT spare_part_id, part_name, part_number, " +
                    "quantity, unit_price, created_at, updated_at " +
                    "FROM spare_parts " +
                    "WHERE spare_part_id = ?";

    private static final String FIND_ALL_SQL =
            "SELECT spare_part_id, part_name, part_number, " +
                    "quantity, unit_price, created_at, updated_at " +
                    "FROM spare_parts " +
                    "ORDER BY part_name";

    private static final String UPDATE_QUANTITY_SQL =
            "UPDATE spare_parts " +
                    "SET quantity = ? " +
                    "WHERE spare_part_id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM spare_parts " +
                    "WHERE spare_part_id = ?";

    public SparePart create(SparePart sparePart)
            throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             INSERT_SQL,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(
                    1,
                    sparePart.getPartName()
            );

            statement.setString(
                    2,
                    sparePart.getPartNumber()
            );

            statement.setInt(
                    3,
                    sparePart.getQuantity()
            );

            statement.setBigDecimal(
                    4,
                    sparePart.getUnitPrice()
            );

            int affectedRows =
                    statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException(
                        "Creating spare part failed."
                );
            }

            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {
                    sparePart.setSparePartId(
                            generatedKeys.getLong(1)
                    );
                } else {
                    throw new SQLException(
                            "Creating spare part failed: " +
                                    "no ID obtained."
                    );
                }
            }

            return sparePart;
        }
    }

    public SparePart findById(Long sparePartId)
            throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             FIND_BY_ID_SQL)) {

            statement.setLong(1, sparePartId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapSparePart(resultSet);
                }

                return null;
            }
        }
    }

    public List<SparePart> findAll()
            throws SQLException {

        List<SparePart> spareParts =
                new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             FIND_ALL_SQL);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {
                spareParts.add(
                        mapSparePart(resultSet)
                );
            }
        }

        return spareParts;
    }

    public boolean updateQuantity(
            Long sparePartId,
            int quantity)
            throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             UPDATE_QUANTITY_SQL)) {

            statement.setInt(1, quantity);
            statement.setLong(2, sparePartId);

            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete(Long sparePartId)
            throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             DELETE_SQL)) {

            statement.setLong(1, sparePartId);

            return statement.executeUpdate() > 0;
        }
    }

    private SparePart mapSparePart(
            ResultSet resultSet)
            throws SQLException {

        SparePart sparePart =
                new SparePart();

        sparePart.setSparePartId(
                resultSet.getLong(
                        "spare_part_id"
                )
        );

        sparePart.setPartName(
                resultSet.getString(
                        "part_name"
                )
        );

        sparePart.setPartNumber(
                resultSet.getString(
                        "part_number"
                )
        );

        sparePart.setQuantity(
                resultSet.getInt(
                        "quantity"
                )
        );

        sparePart.setUnitPrice(
                resultSet.getBigDecimal(
                        "unit_price"
                )
        );

        if (resultSet.getTimestamp(
                "created_at") != null) {

            sparePart.setCreatedAt(
                    resultSet.getTimestamp(
                            "created_at"
                    ).toLocalDateTime()
            );
        }

        if (resultSet.getTimestamp(
                "updated_at") != null) {

            sparePart.setUpdatedAt(
                    resultSet.getTimestamp(
                            "updated_at"
                    ).toLocalDateTime()
            );
        }

        return sparePart;
    }
}