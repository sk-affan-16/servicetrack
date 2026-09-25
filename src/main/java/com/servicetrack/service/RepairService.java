package com.servicetrack.service;

import com.servicetrack.dao.RepairDAO;
import com.servicetrack.model.Repair;

import java.sql.SQLException;
import java.util.List;

public class RepairService {

    private final RepairDAO repairDAO;

    public RepairService() {
        this.repairDAO = new RepairDAO();
    }

    public Repair createRepair(
            Long ticketId,
            String diagnosis,
            String repairNotes)
            throws SQLException {

        validateId(ticketId, "Ticket ID");

        Repair repair = new Repair();

        repair.setTicketId(ticketId);
        repair.setDiagnosis(cleanText(diagnosis));
        repair.setRepairNotes(cleanText(repairNotes));
        repair.setRepairStatus("DIAGNOSING");

        return repairDAO.create(repair);
    }

    public Repair getRepair(
            Long repairId)
            throws SQLException {

        validateId(repairId, "Repair ID");

        return repairDAO.findById(repairId);
    }

    public List<Repair> getRepairsByTicket(
            Long ticketId)
            throws SQLException {

        validateId(ticketId, "Ticket ID");

        return repairDAO.findByTicketId(ticketId);
    }

    public void updateRepair(
            Long repairId,
            String diagnosis,
            String repairNotes,
            String repairStatus)
            throws SQLException {

        validateId(repairId, "Repair ID");
        validateStatus(repairStatus);

        Repair existingRepair =
                repairDAO.findById(repairId);

        if (existingRepair == null) {
            throw new IllegalArgumentException(
                    "Repair not found."
            );
        }

        existingRepair.setDiagnosis(
                cleanText(diagnosis)
        );

        existingRepair.setRepairNotes(
                cleanText(repairNotes)
        );

        existingRepair.setRepairStatus(
                repairStatus
        );

        boolean updated =
                repairDAO.update(existingRepair);

        if (!updated) {
            throw new SQLException(
                    "Unable to update repair."
            );
        }
    }

    public void deleteRepair(
            Long repairId)
            throws SQLException {

        validateId(repairId, "Repair ID");

        Repair existingRepair =
                repairDAO.findById(repairId);

        if (existingRepair == null) {
            throw new IllegalArgumentException(
                    "Repair not found."
            );
        }

        boolean deleted =
                repairDAO.delete(repairId);

        if (!deleted) {
            throw new SQLException(
                    "Unable to delete repair."
            );
        }
    }

    private void validateId(
            Long id,
            String fieldName) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    fieldName +
                            " must be greater than zero."
            );
        }
    }

    private void validateStatus(
            String repairStatus) {

        if (repairStatus == null ||
                repairStatus.isBlank()) {

            throw new IllegalArgumentException(
                    "Repair status is required."
            );
        }

        switch (repairStatus) {

            case "DIAGNOSING":
            case "IN_REPAIR":
            case "WAITING_FOR_PART":
            case "READY":
            case "COMPLETED":
                break;

            default:
                throw new IllegalArgumentException(
                        "Invalid repair status."
                );
        }
    }

    private String cleanText(String value) {

        if (value == null) {
            return null;
        }

        String cleanedValue = value.trim();

        return cleanedValue.isEmpty()
                ? null
                : cleanedValue;
    }
}