
package com.servicetrack.controller;

import com.servicetrack.model.Ticket;
import com.servicetrack.service.TicketService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/technician/tickets")
public class TechnicianTicketsServlet extends HttpServlet {

    private final TicketService ticketService =
            new TicketService();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Check existing session
        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        // Check technician role
        String role = (String) session.getAttribute("role");

        if (!"TECHNICIAN".equals(role)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Technician access required."
            );
            return;
        }

        // Get logged-in technician ID
        Long technicianId =
                (Long) session.getAttribute("userId");

        if (technicianId == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        try {

            // Fetch only tickets assigned to this technician
            List<Ticket> tickets =
                    ticketService.getTicketsByTechnician(
                            technicianId
                    );

            request.setAttribute("tickets", tickets);

            // Load tickets JSP inside common master layout
            request.setAttribute(
                    "contentPage",
                    "/WEB-INF/views/technician/tickets.jsp"
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/common/master.jsp"
            ).forward(request, response);

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load technician tickets.",
                    e
            );
        }
    }
}
