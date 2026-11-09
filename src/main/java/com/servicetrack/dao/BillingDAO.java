
package com.servicetrack.dao;

import com.servicetrack.model.Bill;
import com.servicetrack.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillingDAO {

    // Calculate total spare parts cost for a ticket.
    private static final String PARTS_COST_SQL = """
        SELECT COALESCE(SUM(quantity_used * unit_price), 0)
        FROM repair_part_usage
        WHERE ticket_id = ?
        """;

    // Verify that ticket and its latest repair are completed.
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

    // Check whether the ticket's product has a valid warranty.
    private static final String CHECK_WARRANTY_SQL = """
        SELECT COUNT(*)
        FROM tickets t
        INNER JOIN service_requests sr
            ON t.service_request_id = sr.service_request_id
        INNER JOIN warranties w
            ON sr.product_id = w.product_id
        WHERE t.ticket_id = ?
          AND w.warranty_status = 'ACTIVE'
          AND CURRENT_DATE BETWEEN w.warranty_start
                               AND w.warranty_end
        """;

    // Insert a new bill or update an existing bill.
    private static final String INSERT_BILL_SQL = """
        INSERT INTO bills
        (ticket_id, parts_cost, labour_charge, tax_amount,
         total_amount, payment_status, paid_at)
        VALUES (?, ?, ?, ?, ?, ?,
            CASE WHEN ? = 'PAID'
                 THEN CURRENT_TIMESTAMP
                 ELSE NULL
            END)
        ON DUPLICATE KEY UPDATE
            parts_cost = VALUES(parts_cost),
            labour_charge = VALUES(labour_charge),
            tax_amount = VALUES(tax_amount),
            total_amount = VALUES(total_amount),
            payment_status = VALUES(payment_status),
            paid_at = VALUES(paid_at)
        """;

    private static final String FIND_BY_TICKET_SQL = """
        SELECT * FROM bills
        WHERE ticket_id = ?
        """;

    private static final String FIND_ALL_SQL = """
        SELECT * FROM bills
        ORDER BY generated_at DESC
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
          AND total_amount > 0
        """;

    // Create or recalculate a bill for a completed ticket.
    public Bill createBill(
            Long ticketId,
            BigDecimal labourCharge)
            throws SQLException {

        if (ticketId == null || ticketId <= 0) {
            throw new IllegalArgumentException("Invalid ticket ID.");
        }

        if (labourCharge == null || labourCharge.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Invalid labour charge.");
        }

        try (Connection connection = DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {

                // Step 1: Lock the ticket during bill generation.
                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     "SELECT ticket_id FROM tickets WHERE ticket_id = ? FOR UPDATE")) {

                    statement.setLong(1, ticketId);

                    try (ResultSet rs = statement.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("Ticket not found.");
                        }
                    }
                }

                // Step 2: Verify completed ticket and repair.
                try (PreparedStatement statement =
                             connection.prepareStatement(VERIFY_COMPLETED_SQL)) {

                    statement.setLong(1, ticketId);

                    try (ResultSet rs = statement.executeQuery()) {
                        rs.next();

                        if (rs.getInt(1) == 0) {
                            throw new SQLException(
                                    "Bill can only be generated after the ticket "
                                            + "and latest repair are completed."
                            );
                        }
                    }
                }

                // Step 3: Check warranty validity.
                boolean warrantyValid;

                try (PreparedStatement statement =
                             connection.prepareStatement(CHECK_WARRANTY_SQL)) {

                    statement.setLong(1, ticketId);

                    try (ResultSet rs = statement.executeQuery()) {
                        rs.next();
                        warrantyValid = rs.getInt(1) > 0;
                    }
                }

                // Step 4: Calculate spare parts cost.
                BigDecimal partsCost;

                try (PreparedStatement statement =
                             connection.prepareStatement(PARTS_COST_SQL)) {

                    statement.setLong(1, ticketId);

                    try (ResultSet rs = statement.executeQuery()) {
                        rs.next();
                        partsCost = rs.getBigDecimal(1);

                        if (partsCost == null) {
                            partsCost = BigDecimal.ZERO;
                        }
                    }
                }

                // Step 5: Calculate final bill.
                BigDecimal finalLabourCharge;
                BigDecimal taxAmount = BigDecimal.ZERO.setScale(2);
                BigDecimal totalAmount;
                String paymentStatus;

                if (warrantyValid) {

                    // Full warranty means all charges are free.
                    partsCost = BigDecimal.ZERO.setScale(2);
                    finalLabourCharge = BigDecimal.ZERO.setScale(2);
                    totalAmount = BigDecimal.ZERO.setScale(2);

                    paymentStatus = "PAID";

                } else {

                    // Normal charges when warranty is invalid.
                    finalLabourCharge = labourCharge.setScale(2);

                    partsCost = partsCost.setScale(2);

                    totalAmount = partsCost
                            .add(finalLabourCharge)
                            .add(taxAmount);

                    paymentStatus = "UNPAID";
                }

                // Step 6: Insert new bill or update existing bill.
                try (PreparedStatement statement =
                             connection.prepareStatement(INSERT_BILL_SQL)) {

                    statement.setLong(1, ticketId);
                    statement.setBigDecimal(2, partsCost);
                    statement.setBigDecimal(3, finalLabourCharge);
                    statement.setBigDecimal(4, taxAmount);
                    statement.setBigDecimal(5, totalAmount);
                    statement.setString(6, paymentStatus);
                    statement.setString(7, paymentStatus);

                    statement.executeUpdate();
                }

                // Step 7: Commit transaction.
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

    // Find bill using ticket ID.
    public Bill findByTicketId(Long ticketId)
            throws SQLException {

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

    // Find all bills for admin.
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

    // Find bills belonging to a customer.
    public List<Bill> findBillsByCustomerUserId(Long userId)
            throws SQLException {

        List<Bill> bills = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_CUSTOMER_USER_ID_SQL)) {

            statement.setLong(1, userId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    bills.add(mapBill(rs));
                }
            }
        }

        return bills;
    }

    // Mark a bill as paid.
    public boolean markAsPaid(Long billId)
            throws SQLException {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(UPDATE_PAYMENT_SQL)) {

            statement.setLong(1, billId);

            return statement.executeUpdate() > 0;
        }
    }

    // Convert database result into Bill object.
    private Bill mapBill(ResultSet rs)
            throws SQLException {

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
