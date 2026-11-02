package com.servicetrack.service;

import com.servicetrack.dao.RepairPartUsageDAO;
import com.servicetrack.dao.SparePartDAO;
import com.servicetrack.model.RepairPartUsage;
import com.servicetrack.model.SparePart;
import com.servicetrack.util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class PartsUsageService {

    private final SparePartDAO sparePartDAO;
    private final RepairPartUsageDAO repairPartUsageDAO;

    public PartsUsageService() {
        this.sparePartDAO = new SparePartDAO();
        this.repairPartUsageDAO = new RepairPartUsageDAO();
    }

    public RepairPartUsage usePart(
            Long ticketId,
            Long sparePartId,
            int quantity)
            throws SQLException {

        validateId(ticketId, "Ticket ID");
        validateId(sparePartId, "Spare part ID");

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity used must be greater than zero."
            );
        }

        Connection connection = null;

        try {
            connection = DBConnection.getConnection();

            connection.setAutoCommit(false);

            SparePart sparePart =
                    sparePartDAO.findByIdForUpdate(
                            connection,
                            sparePartId
                    );

            if (sparePart == null) {
                throw new IllegalArgumentException(
                        "Spare part not found."
                );
            }

            if (sparePart.getQuantity() < quantity) {
                throw new IllegalArgumentException(
                        "Insufficient spare-part stock."
                );
            }

            RepairPartUsage usage =
                    new RepairPartUsage();

            usage.setTicketId(ticketId);
            usage.setSparePartId(sparePartId);
            usage.setQuantityUsed(quantity);
            usage.setUnitPrice(
                    sparePart.getUnitPrice()
            );

            repairPartUsageDAO.create(
                    connection,
                    usage
            );

            boolean stockUpdated =
                    sparePartDAO.decreaseQuantity(
                            connection,
                            sparePartId,
                            quantity
                    );

            if (!stockUpdated) {
                throw new SQLException(
                        "Unable to decrease spare-part stock."
                );
            }

            connection.commit();

            return usage;

        } catch (Exception e) {

            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }
            }

            if (e instanceof SQLException) {
                throw (SQLException) e;
            }

            if (e instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) e;
            }

            throw new SQLException(
                    "Unable to complete parts usage transaction.",
                    e
            );

        } finally {

            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ignored) {
                    // Connection is about to close.
                }

                try {
                    connection.close();
                } catch (SQLException ignored) {
                    // Connection cleanup.
                }
            }
        }
    }

    private void validateId(
            Long id,
            String fieldName) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    fieldName + " must be greater than zero."
            );
        }
    }
}