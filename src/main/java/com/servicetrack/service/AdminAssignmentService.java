package com.servicetrack.service;

import com.servicetrack.dao.AdminAssignmentDAO;
import com.servicetrack.dao.UserDAO;
import com.servicetrack.model.AdminTicketView;
import com.servicetrack.model.User;

import java.sql.SQLException;
import java.util.List;

public class AdminAssignmentService {

    private final AdminAssignmentDAO assignmentDAO;
    private final UserDAO userDAO;
    private final TicketService ticketService;

    public AdminAssignmentService() {
        this.assignmentDAO = new AdminAssignmentDAO();
        this.userDAO = new UserDAO();
        this.ticketService = new TicketService();
    }

    public List<AdminTicketView> getAllTickets()
            throws SQLException {

        return assignmentDAO.findAllTickets();
    }

    public List<User> getActiveTechnicians()
            throws SQLException {

        return assignmentDAO.findActiveTechnicians();
    }

    public boolean assignTechnician(
            Long ticketId,
            Long technicianId)
            throws SQLException {

        if (technicianId == null || technicianId <= 0) {
            throw new IllegalArgumentException(
                    "Please select a valid technician."
            );
        }

        User technician = userDAO.findById(technicianId);

        if (technician == null
                || !"TECHNICIAN".equals(technician.getRole())
                || !"ACTIVE".equals(technician.getStatus())) {

            throw new IllegalArgumentException(
                    "Selected user is not an active technician."
            );
        }

        return ticketService.assignTechnician(
                ticketId,
                technicianId
        );
    }
}
