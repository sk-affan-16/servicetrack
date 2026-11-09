package com.servicetrack.dao;

import com.servicetrack.model.Ticket;
import com.servicetrack.model.TicketStatus;
import com.servicetrack.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {

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
     * Normal create method.
     *
     * Technician assignment happens separately,
     * so technician_id is initially NULL.
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
     * Transaction-capable create method.
     *
     * Technician assignment happens separately,
     * so technician_id is initially NULL.
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
     * Assigns a technician to a ticket.
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
            statement.setString(2, TicketStatus.ASSIGNED.name());
            statement.setLong(3, ticketId);

            return statement.executeUpdate() > 0;
        }
    }

    /*
     * Finds all tickets assigned to a technician.
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

        List<Ticket> tickets =
                new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, technicianId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {
                    tickets.add(
                            mapRow(resultSet)
                    );
                }
            }
        }

        return tickets;
    }

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

        List<Ticket> tickets =
                new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, status.name());

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {
                    tickets.add(
                            mapRow(resultSet)
                    );
                }
            }
        }

        return tickets;
    }

    private Ticket mapRow(ResultSet resultSet)
            throws SQLException {

        Ticket ticket = new Ticket();

        ticket.setTicketId(
                resultSet.getLong(
                        "ticket_id"
                )
        );

        ticket.setServiceRequestId(
                resultSet.getLong(
                        "service_request_id"
                )
        );

        long technicianId =
                resultSet.getLong("technician_id");

        if (!resultSet.wasNull()) {
            ticket.setTechnicianId(technicianId);
        }

        ticket.setTicketNumber(
                resultSet.getString(
                        "ticket_number"
                )
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