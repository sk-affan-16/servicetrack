
package com.servicetrack.dao;

import com.servicetrack.model.Ticket;
import com.servicetrack.model.TicketStatus;
import com.servicetrack.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {

    /*
     * Find ticket using ticket ID.
     */
    public Ticket findById(Long ticketId)
            throws SQLException {

        String sql = """
                SELECT ticket_id,
                       service_request_id,
                       technician_id,
                       ticket_number,
                       status,
                       created_at,
                       updated_at
                FROM tickets
                WHERE ticket_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, ticketId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        }

        return null;
    }

    /*
     * Find ticket using service request ID.
     */
    public Ticket findByServiceRequestId(Long serviceRequestId)
            throws SQLException {

        String sql = """
                SELECT ticket_id,
                       service_request_id,
                       technician_id,
                       ticket_number,
                       status,
                       created_at,
                       updated_at
                FROM tickets
                WHERE service_request_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, serviceRequestId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        }

        return null;
    }

    /*
     * Find ticket using ticket number.
     */
    public Ticket findByTicketNumber(String ticketNumber)
            throws SQLException {

        String sql = """
                SELECT ticket_id,
                       service_request_id,
                       technician_id,
                       ticket_number,
                       status,
                       created_at,
                       updated_at
                FROM tickets
                WHERE ticket_number = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, ticketNumber);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        }

        return null;
    }

    /*
     * Create a new ticket.
     */
    public Long create(Ticket ticket)
            throws SQLException {

        String sql = """
                INSERT INTO tickets
                (service_request_id, ticket_number, status)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(
                    1,
                    ticket.getServiceRequestId()
            );

            statement.setString(
                    2,
                    ticket.getTicketNumber()
            );

            statement.setString(
                    3,
                    ticket.getStatus().name()
            );

            int rowsAffected =
                    statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException(
                        "Creating ticket failed."
                );
            }

            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {

                    Long ticketId =
                            generatedKeys.getLong(1);

                    ticket.setTicketId(ticketId);

                    return ticketId;
                }
            }
        }

        throw new SQLException(
                "Creating ticket failed: no ID obtained."
        );
    }

    /*
     * Transaction-capable ticket creation.
     * Uses the caller's existing JDBC connection.
     */
    public Long create(
            Connection connection,
            Ticket ticket)
            throws SQLException {

        String sql = """
                INSERT INTO tickets
                (service_request_id, ticket_number, status)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(
                    1,
                    ticket.getServiceRequestId()
            );

            statement.setString(
                    2,
                    ticket.getTicketNumber()
            );

            statement.setString(
                    3,
                    ticket.getStatus().name()
            );

            int rowsAffected =
                    statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException(
                        "Creating ticket failed."
                );
            }

            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {

                    Long ticketId =
                            generatedKeys.getLong(1);

                    ticket.setTicketId(ticketId);

                    return ticketId;
                }
            }
        }

        throw new SQLException(
                "Creating ticket failed: no ID obtained."
        );
    }

    /*
     * Assign a technician to a ticket.
     */
    public boolean assignTechnician(
            Long ticketId,
            Long technicianId)
            throws SQLException {

        String sql = """
                UPDATE tickets
                SET technician_id = ?,
                    status = ?
                WHERE ticket_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, technicianId);
            statement.setString(
                    2,
                    TicketStatus.ASSIGNED.name()
            );
            statement.setLong(3, ticketId);

            return statement.executeUpdate() > 0;
        }
    }

    /*
     * Find tickets assigned to a particular technician.
     */
    public List<Ticket> findByTechnicianId(
            Long technicianId)
            throws SQLException {

        String sql = """
                SELECT ticket_id,
                       service_request_id,
                       technician_id,
                       ticket_number,
                       status,
                       created_at,
                       updated_at
                FROM tickets
                WHERE technician_id = ?
                ORDER BY created_at DESC
                """;

        List<Ticket> tickets = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, technicianId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {
                    tickets.add(mapRow(resultSet));
                }
            }
        }

        return tickets;
    }

    /*
     * Find tickets eligible for billing.
     *
     * Conditions:
     * 1. Ticket status must be COMPLETED.
     * 2. Latest repair must be COMPLETED.
     * 3. No bill must already exist for the ticket.
     */
    public List<Ticket> findTicketsEligibleForBilling()
            throws SQLException {

        String sql = """
                SELECT t.ticket_id,
                       t.service_request_id,
                       t.technician_id,
                       t.ticket_number,
                       t.status,
                       t.created_at,
                       t.updated_at
                FROM tickets t
                WHERE t.status = 'COMPLETED'

                  AND EXISTS (
                      SELECT 1
                      FROM repairs r
                      WHERE r.ticket_id = t.ticket_id
                        AND r.repair_id = (
                            SELECT r2.repair_id
                            FROM repairs r2
                            WHERE r2.ticket_id = t.ticket_id
                            ORDER BY r2.created_at DESC,
                                     r2.repair_id DESC
                            LIMIT 1
                        )
                        AND r.repair_status = 'COMPLETED'
                  )

                  AND NOT EXISTS (
                      SELECT 1
                      FROM bills b
                      WHERE b.ticket_id = t.ticket_id
                  )

                ORDER BY t.created_at DESC
                """;

        List<Ticket> tickets = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                tickets.add(mapRow(resultSet));
            }
        }

        return tickets;
    }

    /*
     * Update ticket status.
     */
    public boolean updateStatus(
            Long ticketId,
            TicketStatus status)
            throws SQLException {

        String sql = """
                UPDATE tickets
                SET status = ?
                WHERE ticket_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, status.name());
            statement.setLong(2, ticketId);

            return statement.executeUpdate() > 0;
        }
    }

    /*
     * Find tickets using their status.
     */
    public List<Ticket> findByStatus(TicketStatus status)
            throws SQLException {

        String sql = """
                SELECT ticket_id,
                       service_request_id,
                       technician_id,
                       ticket_number,
                       status,
                       created_at,
                       updated_at
                FROM tickets
                WHERE status = ?
                ORDER BY created_at DESC
                """;

        List<Ticket> tickets = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, status.name());

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {
                    tickets.add(mapRow(resultSet));
                }
            }
        }

        return tickets;
    }

    /*
     * Convert ResultSet into Ticket JavaBean.
     */
    private Ticket mapRow(ResultSet resultSet)
            throws SQLException {

        Ticket ticket = new Ticket();

        ticket.setTicketId(
                resultSet.getLong("ticket_id")
        );

        ticket.setServiceRequestId(
                resultSet.getLong("service_request_id")
        );

        long technicianId =
                resultSet.getLong("technician_id");

        if (!resultSet.wasNull()) {
            ticket.setTechnicianId(technicianId);
        }

        ticket.setTicketNumber(
                resultSet.getString("ticket_number")
        );

        ticket.setStatus(
                TicketStatus.valueOf(
                        resultSet.getString("status")
                )
        );

        Timestamp createdAt =
                resultSet.getTimestamp("created_at");

        if (createdAt != null) {
            ticket.setCreatedAt(
                    createdAt.toLocalDateTime()
            );
        }

        Timestamp updatedAt =
                resultSet.getTimestamp("updated_at");

        if (updatedAt != null) {
            ticket.setUpdatedAt(
                    updatedAt.toLocalDateTime()
            );
        }

        return ticket;
    }
}
