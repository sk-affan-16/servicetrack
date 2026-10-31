package com.servicetrack.dao;

import com.servicetrack.model.Bill;
import com.servicetrack.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillingDAO {

    private static final String PARTS_COST_SQL = """
        SELECT COALESCE(SUM(quantity_used * unit_price), 0)
        FROM repair_part_usage
        WHERE ticket_id = ?
        """;

    private static final String VERIFY_COMPLETED_SQL = """
        SELECT COUNT(*)
        FROM tickets t
        WHERE t.ticket_id = ?
          AND t.status = 'COMPLETED'
          AND EXISTS (
              SELECT 1
              FROM repairs r
              WHERE r.ticket_id = t.ticket_id
                AND r.repair_id = (
                    SELECT r2.repair_id
                    FROM repairs r2
                    WHERE r2.ticket_id = t.ticket_id
                    ORDER BY r2.created_at DESC, r2.repair_id DESC
                    LIMIT 1
                )
                AND r.repair_status = 'COMPLETED'
          )
        """;

    private static final String INSERT_BILL_SQL = """
        INSERT INTO bills
        (ticket_id, parts_cost, labour_charge, tax_amount,
         total_amount, payment_status)
        VALUES (?, ?, ?, ?, ?, 'UNPAID')
        """;

    private static final String FIND_BY_TICKET_SQL = """
        SELECT * FROM bills WHERE ticket_id = ?
        """;

    private static final String FIND_ALL_SQL = """
        SELECT * FROM bills ORDER BY generated_at DESC
        """;

    private static final String FIND_BY_CUSTOMER_USER_ID_SQL = """
        SELECT b.*
        FROM bills b
        INNER JOIN tickets t
            ON b.ticket_id = t.ticket_id
        INNER JOIN service_requests sr
            ON t.service_request_id = sr.service_request_id
        INNER JOIN products p
            ON sr.product_id = p.product_id
        INNER JOIN customers c
            ON p.customer_id = c.customer_id
        WHERE c.user_id = ?
        ORDER BY b.generated_at DESC
        """;

    private static final String UPDATE_PAYMENT_SQL = """
        UPDATE bills
        SET payment_status = 'PAID',
            paid_at = CURRENT_TIMESTAMP
        WHERE bill_id = ?
          AND payment_status = 'UNPAID'
        """;

    // Create a bill for a completed ticket
    public Bill createBill(Long ticketId, BigDecimal labourCharge)
            throws SQLException {

        try (Connection connection = DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                // Lock the ticket while generating the bill
                try (PreparedStatement lockStatement =
                             connection.prepareStatement(
                                     "SELECT ticket_id FROM tickets WHERE ticket_id = ? FOR UPDATE")) {

                    lockStatement.setLong(1, ticketId);

                    try (ResultSet rs = lockStatement.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("Ticket not found.");
                        }
                    }
                }

                // Verify ticket and latest repair are completed
                try (PreparedStatement verifyStatement =
                             connection.prepareStatement(VERIFY_COMPLETED_SQL)) {

                    verifyStatement.setLong(1, ticketId);

                    try (ResultSet rs = verifyStatement.executeQuery()) {
                        rs.next();

                        if (rs.getInt(1) == 0) {
                            throw new SQLException(
                                    "Bill can only be generated after the ticket and latest repair are completed."
                            );
                        }
                    }
                }

                // Calculate parts cost
                BigDecimal partsCost;

                try (PreparedStatement partsStatement =
                             connection.prepareStatement(PARTS_COST_SQL)) {

                    partsStatement.setLong(1, ticketId);

                    try (ResultSet rs = partsStatement.executeQuery()) {
                        rs.next();
                        partsCost = rs.getBigDecimal(1);
                    }
                }

                BigDecimal taxAmount = BigDecimal.ZERO.setScale(2);

                BigDecimal totalAmount = partsCost
                        .add(labourCharge)
                        .add(taxAmount);

                // Insert bill
                try (PreparedStatement insertStatement =
                             connection.prepareStatement(
                                     INSERT_BILL_SQL,
                                     Statement.RETURN_GENERATED_KEYS)) {

                    insertStatement.setLong(1, ticketId);
                    insertStatement.setBigDecimal(2, partsCost);
                    insertStatement.setBigDecimal(3, labourCharge);
                    insertStatement.setBigDecimal(4, taxAmount);
                    insertStatement.setBigDecimal(5, totalAmount);

                    insertStatement.executeUpdate();

                    try (ResultSet keys = insertStatement.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("Bill creation failed.");
                        }
                    }
                }

                connection.commit();

            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;

            } finally {
                connection.setAutoCommit(true);
            }
        }

        return findByTicketId(ticketId);
    }

    // Find bill using ticket ID
    public Bill findByTicketId(Long ticketId) throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_TICKET_SQL)) {

            statement.setLong(1, ticketId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapBill(rs);
                }
            }
        }

        return null;
    }

    // Find all bills for admin
    public List<Bill> findAll() throws SQLException {

        List<Bill> bills = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                bills.add(mapBill(rs));
            }
        }

        return bills;
    }

    // Find bills belonging only to the specified customer
    public List<Bill> findBillsByCustomerUserId(Long userId)
            throws SQLException {

        List<Bill> bills = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             FIND_BY_CUSTOMER_USER_ID_SQL)) {

            statement.setLong(1, userId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    bills.add(mapBill(rs));
                }
            }
        }

        return bills;
    }

    // Mark bill as paid
    public boolean markAsPaid(Long billId) throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_PAYMENT_SQL)) {

            statement.setLong(1, billId);

            return statement.executeUpdate() > 0;
        }
    }

    // Convert database result into Bill object
    private Bill mapBill(ResultSet rs) throws SQLException {

        Bill bill = new Bill();

        bill.setBillId(rs.getLong("bill_id"));
        bill.setTicketId(rs.getLong("ticket_id"));
        bill.setPartsCost(rs.getBigDecimal("parts_cost"));
        bill.setLabourCharge(rs.getBigDecimal("labour_charge"));
        bill.setTaxAmount(rs.getBigDecimal("tax_amount"));
        bill.setTotalAmount(rs.getBigDecimal("total_amount"));
        bill.setPaymentStatus(rs.getString("payment_status"));

        Timestamp generatedAt = rs.getTimestamp("generated_at");
        if (generatedAt != null) {
            bill.setGeneratedAt(generatedAt.toLocalDateTime());
        }

        Timestamp paidAt = rs.getTimestamp("paid_at");
        if (paidAt != null) {
            bill.setPaidAt(paidAt.toLocalDateTime());
        }

        return bill;
    }
}