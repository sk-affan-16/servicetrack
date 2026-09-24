package com.servicetrack.service;

import com.servicetrack.dao.CustomerDAO;
import com.servicetrack.dao.ProductDAO;
import com.servicetrack.dao.WarrantyDAO;
import com.servicetrack.model.Customer;
import com.servicetrack.model.Product;
import com.servicetrack.model.Warranty;

import java.sql.SQLException;
import java.time.LocalDate;

public class WarrantyService {

    private final WarrantyDAO warrantyDAO;
    private final ProductDAO productDAO;
    private final CustomerDAO customerDAO;

    public WarrantyService() {
        this.warrantyDAO = new WarrantyDAO();
        this.productDAO = new ProductDAO();
        this.customerDAO = new CustomerDAO();
    }

    public Warranty getWarranty(
            Long userId,
            Long productId) throws SQLException {

        Customer customer = getCustomer(userId);

        Product product = productDAO.findById(productId);

        if (product == null) {
            return null;
        }

        verifyProductOwnership(customer, product);

        return warrantyDAO.findByProductId(productId);
    }

    public Warranty createWarranty(
            Long userId,
            Long productId,
            LocalDate warrantyStart,
            LocalDate warrantyEnd) throws SQLException {

        validateDates(warrantyStart, warrantyEnd);

        Customer customer = getCustomer(userId);

        Product product = productDAO.findById(productId);

        if (product == null) {
            throw new IllegalArgumentException(
                    "Product not found."
            );
        }

        verifyProductOwnership(customer, product);

        if (warrantyDAO.findByProductId(productId) != null) {
            throw new IllegalArgumentException(
                    "Warranty already exists for this product."
            );
        }

        Warranty warranty = new Warranty();

        warranty.setProductId(productId);
        warranty.setWarrantyStart(warrantyStart);
        warranty.setWarrantyEnd(warrantyEnd);
        warranty.setWarrantyStatus("ACTIVE");

        return warrantyDAO.create(warranty);
    }

    public boolean updateWarrantyStatus(
            Long userId,
            Long warrantyId,
            String warrantyStatus) throws SQLException {

        validateStatus(warrantyStatus);

        Customer customer = getCustomer(userId);

        Warranty warranty =
                warrantyDAO.findById(warrantyId);

        if (warranty == null) {
            return false;
        }

        Product product =
                productDAO.findById(warranty.getProductId());

        if (product == null) {
            return false;
        }

        verifyProductOwnership(customer, product);

        return warrantyDAO.updateStatus(
                warrantyId,
                warrantyStatus
        );
    }

    private Customer getCustomer(Long userId)
            throws SQLException {

        if (userId == null) {
            throw new IllegalArgumentException(
                    "User ID is required."
            );
        }

        Customer customer =
                customerDAO.findByUserId(userId);

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer profile not found."
            );
        }

        return customer;
    }

    private void verifyProductOwnership(
            Customer customer,
            Product product) {

        if (!customer.getCustomerId()
                .equals(product.getCustomerId())) {

            throw new IllegalArgumentException(
                    "You are not authorized to access this product."
            );
        }
    }

    private void validateDates(
            LocalDate warrantyStart,
            LocalDate warrantyEnd) {

        if (warrantyStart == null) {
            throw new IllegalArgumentException(
                    "Warranty start date is required."
            );
        }

        if (warrantyEnd == null) {
            throw new IllegalArgumentException(
                    "Warranty end date is required."
            );
        }

        if (warrantyEnd.isBefore(warrantyStart)) {
            throw new IllegalArgumentException(
                    "Warranty end date cannot be before start date."
            );
        }
    }

    private void validateStatus(String warrantyStatus) {

        if (warrantyStatus == null
                || warrantyStatus.isBlank()) {

            throw new IllegalArgumentException(
                    "Warranty status is required."
            );
        }

        if (!"ACTIVE".equals(warrantyStatus)
                && !"EXPIRED".equals(warrantyStatus)
                && !"VOID".equals(warrantyStatus)) {

            throw new IllegalArgumentException(
                    "Invalid warranty status."
            );
        }
    }
}