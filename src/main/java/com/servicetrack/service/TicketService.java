package com.servicetrack.service;

import com.servicetrack.dao.TicketDAO;
import com.servicetrack.model.Ticket;
import com.servicetrack.model.TicketStatus;

import java.sql.SQLException;
import java.util.List;

public class TicketService {

    private final TicketDAO ticketDAO;

    public TicketService() {
        this.ticketDAO = new TicketDAO();
    }

    public Ticket getTicket(Long ticketId)
            throws SQLException {

        validateId(ticketId, "ticket ID");

        return ticketDAO.findById(ticketId);
    }

    public Ticket getTicketByServiceRequest(
            Long serviceRequestId)
            throws SQLException {

        validateId(serviceRequestId, "service request ID");

        return ticketDAO.findByServiceRequestId(
                serviceRequestId
        );
    }

    public Ticket getTicketByNumber(
            String ticketNumber)
            throws SQLException {

        if (ticketNumber == null
                || ticketNumber.isBlank()) {

            throw new IllegalArgumentException(
                    "Ticket number cannot be empty."
            );
        }

        return ticketDAO.findByTicketNumber(
                ticketNumber.trim()
        );
    }

    public boolean updateStatus(
            Long ticketId,
            TicketStatus status)
            throws SQLException {

        validateId(ticketId, "ticket ID");

        if (status == null) {
            throw new IllegalArgumentException(
                    "Ticket status cannot be null."
            );
        }

        Ticket ticket = ticketDAO.findById(ticketId);

        if (ticket == null) {
            throw new IllegalArgumentException(
                    "Ticket not found."
            );
        }

        return ticketDAO.updateStatus(
                ticketId,
                status
        );
    }

    public List<Ticket> getTicketsByStatus(
            TicketStatus status)
            throws SQLException {

        if (status == null) {
            throw new IllegalArgumentException(
                    "Ticket status cannot be null."
            );
        }

        return ticketDAO.findByStatus(status);
    }

    private void validateId(
            Long id,
            String fieldName) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Invalid " + fieldName + "."
            );
        }
    }
}