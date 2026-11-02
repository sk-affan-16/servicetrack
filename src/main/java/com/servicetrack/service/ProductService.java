package com.servicetrack.service;

import com.servicetrack.dao.CustomerDAO;
import com.servicetrack.dao.ProductDAO;
import com.servicetrack.model.Customer;
import com.servicetrack.model.Product;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ProductService {

    private final ProductDAO productDAO;
    private final CustomerDAO customerDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
        this.customerDAO = new CustomerDAO();
    }

    public Product registerProduct(
            Long userId,
            String productName,
            String brand,
            String modelNumber,
            String serialNumber,
            LocalDate purchaseDate) throws SQLException {

        validateProductInput(
                productName,
                brand,
                serialNumber,
                purchaseDate
        );

        Customer customer = customerDAO.findByUserId(userId);

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer profile not found."
            );
        }

        if (productDAO.findBySerialNumber(serialNumber.trim()) != null) {
            throw new IllegalArgumentException(
                    "Serial number already exists."
            );
        }

        Product product = new Product();

        product.setCustomerId(customer.getCustomerId());
        product.setProductName(productName.trim());
        product.setBrand(brand.trim());
        product.setModelNumber(
                modelNumber == null || modelNumber.isBlank()
                        ? null
                        : modelNumber.trim()
        );
        product.setSerialNumber(serialNumber.trim());
        product.setPurchaseDate(purchaseDate);

        return productDAO.create(product);
    }

    public Product getProduct(
            Long productId,
            Long userId) throws SQLException {

        if (productId == null) {
            throw new IllegalArgumentException(
                    "Product ID is required."
            );
        }

        Customer customer = customerDAO.findByUserId(userId);

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer profile not found."
            );
        }

        Product product = productDAO.findById(productId);

        if (product == null) {
            return null;
        }

        if (!customer.getCustomerId()
                .equals(product.getCustomerId())) {

            throw new IllegalArgumentException(
                    "You are not authorized to access this product."
            );
        }

        return product;
    }

    public List<Product> getCustomerProducts(
            Long userId) throws SQLException {

        Customer customer = customerDAO.findByUserId(userId);

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer profile not found."
            );
        }

        return productDAO.findByCustomerId(
                customer.getCustomerId()
        );
    }

    public boolean updateProduct(
            Long userId,
            Product product) throws SQLException {

        if (product == null) {
            throw new IllegalArgumentException(
                    "Product is required."
            );
        }

        validateProductInput(
                product.getProductName(),
                product.getBrand(),
                product.getSerialNumber(),
                product.getPurchaseDate()
        );

        Customer customer = customerDAO.findByUserId(userId);

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer profile not found."
            );
        }

        if (!customer.getCustomerId()
                .equals(product.getCustomerId())) {

            throw new IllegalArgumentException(
                    "You are not authorized to update this product."
            );
        }

        Product existingProduct =
                productDAO.findBySerialNumber(
                        product.getSerialNumber().trim()
                );

        if (existingProduct != null
                && !existingProduct.getProductId()
                .equals(product.getProductId())) {

            throw new IllegalArgumentException(
                    "Serial number already exists."
            );
        }

        return productDAO.update(product);
    }

    public boolean deleteProduct(
            Long userId,
            Long productId) throws SQLException {

        if (productId == null) {
            throw new IllegalArgumentException(
                    "Product ID is required."
            );
        }

        Customer customer = customerDAO.findByUserId(userId);

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer profile not found."
            );
        }

        Product product = productDAO.findById(productId);

        if (product == null) {
            return false;
        }

        if (!customer.getCustomerId()
                .equals(product.getCustomerId())) {

            throw new IllegalArgumentException(
                    "You are not authorized to delete this product."
            );
        }

        return productDAO.delete(
                productId,
                customer.getCustomerId()
        );
    }

    private void validateProductInput(
            String productName,
            String brand,
            String serialNumber,
            LocalDate purchaseDate) {

        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException(
                    "Product name is required."
            );
        }

        if (brand == null || brand.isBlank()) {
            throw new IllegalArgumentException(
                    "Brand is required."
            );
        }

        if (serialNumber == null || serialNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "Serial number is required."
            );
        }

        if (purchaseDate == null) {
            throw new IllegalArgumentException(
                    "Purchase date is required."
            );
        }

        if (productName.trim().length() > 100) {
            throw new IllegalArgumentException(
                    "Product name is too long."
            );
        }

        if (brand.trim().length() > 100) {
            throw new IllegalArgumentException(
                    "Brand is too long."
            );
        }

        if (serialNumber.trim().length() > 100) {
            throw new IllegalArgumentException(
                    "Serial number is too long."
            );
        }

        if (purchaseDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Purchase date cannot be in the future."
            );
        }
    }
}