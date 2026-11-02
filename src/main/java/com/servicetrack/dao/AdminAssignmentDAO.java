package com.servicetrack.dao;

import com.servicetrack.model.AdminTicketView;
import com.servicetrack.model.User;
import com.servicetrack.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminAssignmentDAO {

    private static final String FIND_ALL_TICKETS_SQL = """
            SELECT
                t.ticket_id,
                t.ticket_number,
                t.status,
                t.technician_id,
                t.created_at,

                sr.complaint_description,

                p.product_name,
                p.brand,
                p.model_number,
                p.serial_number,

                customer_user.full_name AS customer_name,
                customer_user.phone AS customer_phone,

                technician.full_name AS technician_name

            FROM tickets t

            INNER JOIN service_requests sr
                ON t.service_request_id = sr.service_request_id

            INNER JOIN products p
                ON sr.product_id = p.product_id

            INNER JOIN customers c
                ON p.customer_id = c.customer_id

            INNER JOIN users customer_user
                ON c.user_id = customer_user.user_id

            LEFT JOIN users technician
                ON t.technician_id = technician.user_id

            ORDER BY
                CASE WHEN t.status = 'PENDING' THEN 0 ELSE 1 END,
                t.created_at DESC
            """;

    private static final String FIND_ACTIVE_TECHNICIANS_SQL = """
            SELECT
                user_id,
                full_name,
                email,
                phone,
                username,
                password_hash,
                role,
                status,
                created_at,
                updated_at
            FROM users
            WHERE role = 'TECHNICIAN'
              AND status = 'ACTIVE'
            ORDER BY full_name
            """;

    public List<AdminTicketView> findAllTickets()
            throws SQLException {

        List<AdminTicketView> tickets = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_ALL_TICKETS_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                AdminTicketView ticket = new AdminTicketView();

                ticket.setTicketId(
                        resultSet.getLong("ticket_id")
                );

                ticket.setTicketNumber(
                        resultSet.getString("ticket_number")
                );

                ticket.setStatus(
                        resultSet.getString("status")
                );

                ticket.setComplaintDescription(
                        resultSet.getString("complaint_description")
                );

                ticket.setProductName(
                        resultSet.getString("product_name")
                );

                ticket.setBrand(
                        resultSet.getString("brand")
                );

                ticket.setModelNumber(
                        resultSet.getString("model_number")
                );

                ticket.setSerialNumber(
                        resultSet.getString("serial_number")
                );

                ticket.setCustomerName(
                        resultSet.getString("customer_name")
                );

                ticket.setCustomerPhone(
                        resultSet.getString("customer_phone")
                );

                long technicianId =
                        resultSet.getLong("technician_id");

                if (!resultSet.wasNull()) {
                    ticket.setTechnicianId(technicianId);
                }

                ticket.setTechnicianName(
                        resultSet.getString("technician_name")
                );

                if (resultSet.getTimestamp("created_at") != null) {
                    ticket.setCreatedAt(
                            resultSet.getTimestamp("created_at")
                                    .toLocalDateTime()
                    );
                }

                tickets.add(ticket);
            }
        }

        return tickets;
    }

    public List<User> findActiveTechnicians()
            throws SQLException {

        List<User> technicians = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             FIND_ACTIVE_TECHNICIANS_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                User technician = new User();

                technician.setUserId(
                        resultSet.getLong("user_id")
                );

                technician.setFullName(
                        resultSet.getString("full_name")
                );

                technician.setEmail(
                        resultSet.getString("email")
                );

                technician.setPhone(
                        resultSet.getString("phone")
                );

                technician.setUsername(
                        resultSet.getString("username")
                );

                technician.setPasswordHash(
                        resultSet.getString("password_hash")
                );

                technician.setRole(
                        resultSet.getString("role")
                );

                technician.setStatus(
                        resultSet.getString("status")
                );

                if (resultSet.getTimestamp("created_at") != null) {
                    technician.setCreatedAt(
                            resultSet.getTimestamp("created_at")
                                    .toLocalDateTime()
                    );
                }

                if (resultSet.getTimestamp("updated_at") != null) {
                    technician.setUpdatedAt(
                            resultSet.getTimestamp("updated_at")
                                    .toLocalDateTime()
                    );
                }

                technicians.add(technician);
            }
        }

        return technicians;
    }
}
