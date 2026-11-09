
package com.servicetrack.service;

import com.servicetrack.dao.BillingDAO;
import com.servicetrack.model.Bill;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;

public class BillingService {

    private final BillingDAO billingDAO;

    public BillingService() {
        this.billingDAO = new BillingDAO();
    }

    // Generate a bill for a completed ticket.
    // BillingDAO checks warranty and calculates the final amount.
    public Bill generateBill(Long ticketId, BigDecimal labourCharge)
            throws SQLException {

        validateId(ticketId, "Ticket ID");

        if (labourCharge == null) {
            throw new IllegalArgumentException(
                    "Labour charge is required."
            );
        }

        if (labourCharge.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Labour charge cannot be negative."
            );
        }

        labourCharge = labourCharge.setScale(
                2, RoundingMode.HALF_UP
        );

        if (labourCharge.precision() > 10) {
            throw new IllegalArgumentException(
                    "Labour charge exceeds the allowed amount."
            );
        }

        return billingDAO.createBill(ticketId, labourCharge);
    }

    // Get bill using ticket ID.
    public Bill getBillByTicketId(Long ticketId)
            throws SQLException {

        validateId(ticketId, "Ticket ID");

        return billingDAO.findByTicketId(ticketId);
    }

    // Get all bills for admin.
    public List<Bill> getAllBills() throws SQLException {

        return billingDAO.findAll();
    }

    // Get bills belonging to a particular customer.
    public List<Bill> getBillsByCustomer(Long userId)
            throws SQLException {

        validateId(userId, "User ID");

        return billingDAO.findBillsByCustomerUserId(userId);
    }

    // Mark a bill as paid.
    public void markBillAsPaid(Long billId)
            throws SQLException {

        validateId(billId, "Bill ID");

        boolean updated = billingDAO.markAsPaid(billId);

        if (!updated) {
            throw new IllegalArgumentException(
                    "Bill not found or already paid."
            );
        }
    }

    // Validate database IDs.
    private void validateId(Long id, String fieldName) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Invalid " + fieldName + "."
            );
        }
    }
}
