package com.servicetrack.dao;

import com.servicetrack.model.RepairPartUsage;
import com.servicetrack.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RepairPartUsageDAO {

    private static final String INSERT_SQL =
            "INSERT INTO repair_part_usage " +
                    "(ticket_id, spare_part_id, quantity_used, unit_price) " +
                    "VALUES (?, ?, ?, ?)";

    private static final String FIND_BY_TICKET_ID_SQL =
            "SELECT usage_id, ticket_id, spare_part_id, " +
                    "quantity_used, unit_price, used_at " +
                    "FROM repair_part_usage " +
                    "WHERE ticket_id = ? " +
                    "ORDER BY used_at";

    private static final String DELETE_SQL =
            "DELETE FROM repair_part_usage " +
                    "WHERE usage_id = ?";

    public RepairPartUsage create(
            RepairPartUsage usage)
            throws SQLException {

        try (Connection connection =
                     DBConnection.getConnection()) {

            return create(connection, usage);
        }
    }

    public RepairPartUsage create(
            Connection connection,
            RepairPartUsage usage)
            throws SQLException {

        try (PreparedStatement statement =
                     connection.prepareStatement(
                             INSERT_SQL,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(
                    1,
                    usage.getTicketId()
            );

            statement.setLong(
                    2,
                    usage.getSparePartId()
            );

            statement.setInt(
                    3,
                    usage.getQuantityUsed()
            );

            statement.setBigDecimal(
                    4,
                    usage.getUnitPrice()
            );

            int affectedRows =
                    statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException(
                        "Creating repair part usage failed."
                );
            }

            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {
                    usage.setUsageId(
                            generatedKeys.getLong(1)
                    );
                } else {
                    throw new SQLException(
                            "Creating repair part usage failed: " +
                                    "no ID obtained."
                    );
                }
            }

            return usage;
        }
    }

    public List<RepairPartUsage> findByTicketId(
            Long ticketId)
            throws SQLException {

        List<RepairPartUsage> usages =
                new ArrayList<>();

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             FIND_BY_TICKET_ID_SQL)) {

            statement.setLong(1, ticketId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {
                    usages.add(
                            mapRepairPartUsage(resultSet)
                    );
                }
            }
        }

        return usages;
    }

    public boolean delete(Long usageId)
            throws SQLException {

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             DELETE_SQL)) {

            statement.setLong(1, usageId);

            return statement.executeUpdate() > 0;
        }
    }

    private RepairPartUsage mapRepairPartUsage(
            ResultSet resultSet)
            throws SQLException {

        RepairPartUsage usage =
                new RepairPartUsage();

        usage.setUsageId(
                resultSet.getLong("usage_id")
        );

        usage.setTicketId(
                resultSet.getLong("ticket_id")
        );

        usage.setSparePartId(
                resultSet.getLong("spare_part_id")
        );

        usage.setQuantityUsed(
                resultSet.getInt("quantity_used")
        );

        usage.setUnitPrice(
                resultSet.getBigDecimal("unit_price")
        );

        if (resultSet.getTimestamp("used_at") != null) {
            usage.setUsedAt(
                    resultSet.getTimestamp("used_at")
                            .toLocalDateTime()
            );
        }

        return usage;
    }
}