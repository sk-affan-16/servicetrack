package com.servicetrack.service;

import com.servicetrack.dao.ServiceRequestDAO;
import com.servicetrack.dao.TicketDAO;
import com.servicetrack.model.ServiceRequest;
import com.servicetrack.model.Ticket;
import com.servicetrack.model.TicketStatus;
import com.servicetrack.util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ServiceRequestService {

    private final ServiceRequestDAO serviceRequestDAO;
    private final TicketDAO ticketDAO;

    public ServiceRequestService() {
        this.serviceRequestDAO = new ServiceRequestDAO();
        this.ticketDAO = new TicketDAO();
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

    /*
     * Creates only a service request.
     *
     * This method is kept for normal service request creation.
     */
    public Long createServiceRequest(
            Long productId,
            String complaintDescription)
            throws SQLException {

        validateProductId(productId);

        String complaint =
                validateComplaint(complaintDescription);

        ServiceRequest serviceRequest =
                new ServiceRequest();

        serviceRequest.setProductId(productId);
        serviceRequest.setComplaintDescription(complaint);

        return serviceRequestDAO.create(serviceRequest);
    }

    /*
     * Creates a service request and its ticket
     * inside one JDBC transaction.
     *
     * If either database operation fails,
     * the complete transaction is rolled back.
     */
    public Ticket createServiceRequestWithTicket(
            Long productId,
            String complaintDescription)
            throws SQLException {

        validateProductId(productId);

        String complaint =
                validateComplaint(complaintDescription);

        ServiceRequest serviceRequest =
                new ServiceRequest();

        serviceRequest.setProductId(productId);
        serviceRequest.setComplaintDescription(complaint);

        Ticket ticket =
                new Ticket();

        ticket.setTicketNumber(
                generateTicketNumber()
        );

        ticket.setStatus(
                TicketStatus.PENDING
        );

        try (Connection connection =
                     DBConnection.getConnection()) {

            try {

                connection.setAutoCommit(false);

                Long serviceRequestId =
                        serviceRequestDAO.create(
                                connection,
                                serviceRequest
                        );

                ticket.setServiceRequestId(
                        serviceRequestId
                );

                ticketDAO.create(
                        connection,
                        ticket
                );

                connection.commit();

                return ticket;

            } catch (SQLException | RuntimeException e) {

                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }

                throw e;
            }
        }
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

        String complaint =
                validateComplaint(complaintDescription);

        ServiceRequest serviceRequest =
                serviceRequestDAO.findById(
                        serviceRequestId
                );

        if (serviceRequest == null) {
            throw new IllegalArgumentException(
                    "Service request not found."
            );
        }

        serviceRequest.setComplaintDescription(
                complaint
        );

        return serviceRequestDAO.update(
                serviceRequest
        );
    }

    public boolean deleteServiceRequest(
            Long serviceRequestId)
            throws SQLException {

        if (serviceRequestId == null || serviceRequestId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid service request ID."
            );
        }

        return serviceRequestDAO.delete(
                serviceRequestId
        );
    }

    private void validateProductId(Long productId) {

        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid product ID."
            );
        }
    }

    private String validateComplaint(
            String complaintDescription) {

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

        return complaint;
    }

    private String generateTicketNumber() {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "yyyyMMdd-HHmmss-SSS"
                );

        return "TKT-"
                + LocalDateTime.now().format(formatter);
    }
}