package com.servicetrack.service;

import com.servicetrack.dao.CustomerDAO;
import com.servicetrack.model.Customer;

import java.sql.SQLException;

public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService() {
        this.customerDAO = new CustomerDAO();
    }

    /**
     * Finds a customer profile using the authenticated user's ID.
     */
    public Customer getCustomerByUserId(Long userId)
            throws SQLException {

        validateUserId(userId);

        return customerDAO.findByUserId(userId);
    }

    /**
     * Creates a customer profile for an authenticated user.
     */
    public Customer createCustomer(Customer customer)
            throws SQLException {

        validateCustomer(customer);

        if (customerDAO.findByUserId(
                customer.getUserId()) != null) {

            throw new IllegalArgumentException(
                    "Customer profile already exists."
            );
        }

        validateFields(customer);

        return customerDAO.create(customer);
    }

    /**
     * Updates an existing customer profile.
     */
    public boolean updateCustomer(Customer customer)
            throws SQLException {

        validateCustomer(customer);
        validateFields(customer);

        return customerDAO.update(customer);
    }

    private void validateCustomer(Customer customer) {

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer is required."
            );
        }

        validateUserId(customer.getUserId());
    }

    private void validateUserId(Long userId) {

        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException(
                    "Valid user ID is required."
            );
        }
    }

    private void validateFields(Customer customer) {

        if (customer.getAddress() != null
                && customer.getAddress().length() > 255) {

            throw new IllegalArgumentException(
                    "Address is too long."
            );
        }

        if (customer.getCity() != null
                && customer.getCity().length() > 100) {

            throw new IllegalArgumentException(
                    "City is too long."
            );
        }

        if (customer.getState() != null
                && customer.getState().length() > 100) {

            throw new IllegalArgumentException(
                    "State is too long."
            );
        }

        if (customer.getPincode() != null
                && customer.getPincode().length() > 10) {

            throw new IllegalArgumentException(
                    "Pincode is too long."
            );
        }
    }
}