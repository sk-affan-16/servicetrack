package com.servicetrack.service;

import com.servicetrack.dao.SparePartDAO;
import com.servicetrack.model.SparePart;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class InventoryService {

    private final SparePartDAO sparePartDAO;

    public InventoryService() {
        this.sparePartDAO = new SparePartDAO();
    }

    public SparePart addSparePart(
            String partName,
            String partNumber,
            int quantity,
            BigDecimal unitPrice)
            throws SQLException {

        validatePartName(partName);
        validatePartNumber(partNumber);
        validateQuantity(quantity);
        validateUnitPrice(unitPrice);

        SparePart sparePart = new SparePart();

        sparePart.setPartName(partName.trim());
        sparePart.setPartNumber(partNumber.trim());
        sparePart.setQuantity(quantity);
        sparePart.setUnitPrice(unitPrice);

        return sparePartDAO.create(sparePart);
    }

    public SparePart getSparePart(Long sparePartId)
            throws SQLException {

        validateId(sparePartId);

        return sparePartDAO.findById(sparePartId);
    }

    public List<SparePart> getAllSpareParts()
            throws SQLException {

        return sparePartDAO.findAll();
    }

    public void updateStock(
            Long sparePartId,
            int quantity)
            throws SQLException {

        validateId(sparePartId);
        validateQuantity(quantity);

        SparePart existingPart =
                sparePartDAO.findById(sparePartId);

        if (existingPart == null) {
            throw new IllegalArgumentException(
                    "Spare part not found."
            );
        }

        boolean updated =
                sparePartDAO.updateQuantity(
                        sparePartId,
                        quantity
                );

        if (!updated) {
            throw new SQLException(
                    "Unable to update spare part quantity."
            );
        }
    }

    public void deleteSparePart(
            Long sparePartId)
            throws SQLException {

        validateId(sparePartId);

        SparePart existingPart =
                sparePartDAO.findById(sparePartId);

        if (existingPart == null) {
            throw new IllegalArgumentException(
                    "Spare part not found."
            );
        }

        boolean deleted =
                sparePartDAO.delete(sparePartId);

        if (!deleted) {
            throw new SQLException(
                    "Unable to delete spare part."
            );
        }
    }

    private void validateId(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "ID must be greater than zero."
            );
        }
    }

    private void validatePartName(String partName) {

        if (partName == null ||
                partName.isBlank()) {

            throw new IllegalArgumentException(
                    "Part name is required."
            );
        }
    }

    private void validatePartNumber(
            String partNumber) {

        if (partNumber == null ||
                partNumber.isBlank()) {

            throw new IllegalArgumentException(
                    "Part number is required."
            );
        }
    }

    private void validateQuantity(
            int quantity) {

        if (quantity < 0) {
            throw new IllegalArgumentException(
                    "Quantity cannot be negative."
            );
        }
    }

    private void validateUnitPrice(
            BigDecimal unitPrice) {

        if (unitPrice == null ||
                unitPrice.compareTo(
                        BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "Unit price cannot be negative."
            );
        }
    }
}