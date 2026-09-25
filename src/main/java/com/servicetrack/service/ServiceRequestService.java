package com.servicetrack.service;

import com.servicetrack.dao.ServiceRequestDAO;
import com.servicetrack.model.ServiceRequest;

import java.sql.SQLException;
import java.util.List;

public class ServiceRequestService {

    private final ServiceRequestDAO serviceRequestDAO;

    public ServiceRequestService() {
        this.serviceRequestDAO = new ServiceRequestDAO();
    }

    public ServiceRequest getServiceRequest(Long serviceRequestId)
            throws SQLException {

        if (serviceRequestId == null || serviceRequestId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid service request ID."
            );
        }

        return serviceRequestDAO.findById(serviceRequestId);
    }

    public List<ServiceRequest> getServiceRequestsByProduct(
            Long productId)
            throws SQLException {

        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid product ID."
            );
        }

        return serviceRequestDAO.findByProductId(productId);
    }

    public Long createServiceRequest(
            Long productId,
            String complaintDescription)
            throws SQLException {

        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid product ID."
            );
        }

        if (complaintDescription == null
                || complaintDescription.isBlank()) {

            throw new IllegalArgumentException(
                    "Complaint description cannot be empty."
            );
        }

        String complaint =
                complaintDescription.trim();

        if (complaint.length() > 1000) {
            throw new IllegalArgumentException(
                    "Complaint description cannot exceed 1000 characters."
            );
        }

        ServiceRequest serviceRequest =
                new ServiceRequest();

        serviceRequest.setProductId(productId);
        serviceRequest.setComplaintDescription(complaint);

        return serviceRequestDAO.create(serviceRequest);
    }

    public boolean updateComplaint(
            Long serviceRequestId,
            String complaintDescription)
            throws SQLException {

        if (serviceRequestId == null || serviceRequestId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid service request ID."
            );
        }

        if (complaintDescription == null
                || complaintDescription.isBlank()) {

            throw new IllegalArgumentException(
                    "Complaint description cannot be empty."
            );
        }

        String complaint =
                complaintDescription.trim();

        if (complaint.length() > 1000) {
            throw new IllegalArgumentException(
                    "Complaint description cannot exceed 1000 characters."
            );
        }

        ServiceRequest serviceRequest =
                serviceRequestDAO.findById(serviceRequestId);

        if (serviceRequest == null) {
            throw new IllegalArgumentException(
                    "Service request not found."
            );
        }

        serviceRequest.setComplaintDescription(complaint);

        return serviceRequestDAO.update(serviceRequest);
    }

    public boolean deleteServiceRequest(
            Long serviceRequestId)
            throws SQLException {

        if (serviceRequestId == null || serviceRequestId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid service request ID."
            );
        }

        return serviceRequestDAO.delete(serviceRequestId);
    }
}