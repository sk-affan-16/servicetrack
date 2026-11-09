package com.servicetrack.dao;

import com.servicetrack.model.Repair;
import com.servicetrack.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RepairDAO {

    private static final String INSERT_SQL =
            "INSERT INTO repairs " +
                    "(ticket_id, diagnosis, repair_notes, repair_status) " +
                    "VALUES (?, ?, ?, ?)";

    private static final String FIND_BY_ID_SQL =
            "SELECT repair_id, ticket_id, diagnosis, repair_notes, " +
                    "repair_status, created_at, updated_at " +
                    "FROM repairs " +
                    "WHERE repair_id = ?";

    private static final String FIND_BY_TICKET_ID_SQL =
            "SELECT repair_id, ticket_id, diagnosis, repair_notes, " +
                    "repair_status, created_at, updated_at " +
                    "FROM repairs " +
                    "WHERE ticket_id = ? " +
                    "ORDER BY created_at DESC";

    private static final String UPDATE_SQL =
            "UPDATE repairs " +
                    "SET diagnosis = ?, " +
                    "repair_notes = ?, " +
                    "repair_status = ? " +
                    "WHERE repair_id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM repairs " +
                    "WHERE repair_id = ?";

    public Repair create(Repair repair) throws SQLException {
        String sql = INSERT_SQL;
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement statement = connection.prepareStatement(
                        sql, Statement.RETURN_GENERATED_KEYS)) {
                    statement.setLong(1, repair.getTicketId());
                    statement.setString(2, repair.getDiagnosis());
                    statement.setString(3, repair.getRepairNotes());
                    statement.setString(4, repair.getRepairStatus());
                    if (statement.executeUpdate() == 0) {
                        throw new SQLException("Creating repair failed.");
                    }
                    try (ResultSet keys = statement.getGeneratedKeys()) {
                        if (!keys.next()) throw new SQLException("Repair ID was not generated.");
                        repair.setRepairId(keys.getLong(1));
                    }
                }
                synchronizeTicketStatus(connection, repair.getTicketId(), repair.getRepairStatus());
                connection.commit();
                return repair;
            } catch (SQLException | RuntimeException ex) {
                connection.rollback();
                throw ex;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public Repair findById(Long repairId)
            throws SQLException {

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             FIND_BY_ID_SQL)) {

            statement.setLong(
                    1,
                    repairId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapRepair(resultSet);
                }

                return null;
            }
        }
    }

    public List<Repair> findByTicketId(
            Long ticketId)
            throws SQLException {

        List<Repair> repairs =
                new ArrayList<>();

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             FIND_BY_TICKET_ID_SQL)) {

            statement.setLong(
                    1,
                    ticketId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    repairs.add(
                            mapRepair(resultSet)
                    );
                }
            }
        }

        return repairs;
    }

    public boolean update(Repair repair) throws SQLException {
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int rows;
                try (PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
                    statement.setString(1, repair.getDiagnosis());
                    statement.setString(2, repair.getRepairNotes());
                    statement.setString(3, repair.getRepairStatus());
                    statement.setLong(4, repair.getRepairId());
                    rows = statement.executeUpdate();
                }
                if (rows == 0) {
                    connection.rollback();
                    return false;
                }
                synchronizeTicketStatus(connection, repair.getTicketId(), repair.getRepairStatus());
                connection.commit();
                return true;
            } catch (SQLException | RuntimeException ex) {
                connection.rollback();
                throw ex;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    private void synchronizeTicketStatus(Connection connection, Long ticketId, String repairStatus)
            throws SQLException {
        String latestStatusSql = """
                SELECT repair_status
                FROM repairs
                WHERE ticket_id = ?
                ORDER BY created_at DESC, repair_id DESC
                LIMIT 1
                """;
        String latestStatus = repairStatus;
        try (PreparedStatement latest = connection.prepareStatement(latestStatusSql)) {
            latest.setLong(1, ticketId);
            try (ResultSet rs = latest.executeQuery()) {
                if (rs.next()) latestStatus = rs.getString(1);
            }
        }

        String sql = "UPDATE tickets SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE ticket_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, latestStatus);
            statement.setLong(2, ticketId);
            if (statement.executeUpdate() == 0) {
                throw new SQLException("Ticket status could not be synchronized with repair status.");
            }
        }
    }

    public boolean delete(Long repairId)
            throws SQLException {

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             DELETE_SQL)) {

            statement.setLong(
                    1,
                    repairId
            );

            return statement.executeUpdate() > 0;
        }
    }

    private Repair mapRepair(
            ResultSet resultSet)
            throws SQLException {

        Repair repair =
                new Repair();

        repair.setRepairId(
                resultSet.getLong(
                        "repair_id"
                )
        );

        repair.setTicketId(
                resultSet.getLong(
                        "ticket_id"
                )
        );

        repair.setDiagnosis(
                resultSet.getString(
                        "diagnosis"
                )
        );

        repair.setRepairNotes(
                resultSet.getString(
                        "repair_notes"
                )
        );

        repair.setRepairStatus(
                resultSet.getString(
                        "repair_status"
                )
        );

        if (resultSet.getTimestamp(
                "created_at") != null) {

            repair.setCreatedAt(
                    resultSet.getTimestamp(
                            "created_at"
                    ).toLocalDateTime()
            );
        }

        if (resultSet.getTimestamp(
                "updated_at") != null) {

            repair.setUpdatedAt(
                    resultSet.getTimestamp(
                            "updated_at"
                    ).toLocalDateTime()
            );
        }

        return repair;
    }
}