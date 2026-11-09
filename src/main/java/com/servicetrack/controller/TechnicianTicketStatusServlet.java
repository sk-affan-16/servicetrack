package com.servicetrack.controller;

import com.servicetrack.model.Ticket;
import com.servicetrack.model.TicketStatus;
import com.servicetrack.service.TicketService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/technician/ticket/status")
public class TechnicianTicketStatusServlet extends HttpServlet {

    private final TicketService ticketService = new TicketService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"TECHNICIAN".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Long technicianId = (Long) session.getAttribute("userId");
        String ticketIdParameter = request.getParameter("ticketId");
        String statusParameter = request.getParameter("status");

        try {
            if (ticketIdParameter == null || statusParameter == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ticket ID and status are required.");
                return;
            }

            Long ticketId = Long.parseLong(ticketIdParameter);
            Ticket ticket = ticketService.getTicket(ticketId);

            if (ticket == null || ticket.getTechnicianId() == null
                    || !ticket.getTechnicianId().equals(technicianId)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not authorized to update this ticket.");
                return;
            }

            TicketStatus status = TicketStatus.valueOf(statusParameter.trim().toUpperCase());

            // Repair completion must happen through RepairDAO so both records
            // are updated together in one JDBC transaction.
            if (status == TicketStatus.COMPLETED) {
                response.sendRedirect(request.getContextPath()
                        + "/technician/repair?ticketId=" + ticketId);
                return;
            }

            boolean updated = ticketService.updateStatus(ticketId, status);
            if (!updated) {
                response.sendRedirect(request.getContextPath()
                        + "/technician/repair?ticketId=" + ticketId + "&error=status");
                return;
            }

            response.sendRedirect(request.getContextPath()
                    + "/technician/ticket?ticketId=" + ticketId);

        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ticket ID or status.");
        } catch (SQLException e) {
            throw new ServletException("Unable to update ticket status.", e);
        }
    }
}
