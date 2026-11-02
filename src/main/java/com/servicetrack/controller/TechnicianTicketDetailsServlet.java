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

@WebServlet("/technician/ticket")
public class TechnicianTicketDetailsServlet extends HttpServlet {

    private final TicketService ticketService =
            new TicketService();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        Long technicianId =
                (Long) session.getAttribute("userId");

        if (technicianId == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        String ticketIdParameter =
                request.getParameter("ticketId");

        try {

            Long ticketId =
                    Long.parseLong(ticketIdParameter);

            Ticket ticket =
                    ticketService.getTicket(ticketId);

            if (ticket == null) {
                request.setAttribute(
                        "error",
                        "Ticket not found."
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/technician/ticket-details.jsp"
                ).forward(request, response);

                return;
            }

            if (ticket.getTechnicianId() == null
                    || !ticket.getTechnicianId()
                    .equals(technicianId)) {

                request.setAttribute(
                        "error",
                        "You are not authorized to access this ticket."
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/technician/ticket-details.jsp"
                ).forward(request, response);

                return;
            }

            request.setAttribute(
                    "ticket",
                    ticket
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/technician/ticket-details.jsp"
            ).forward(request, response);

        } catch (NumberFormatException e) {

            request.setAttribute(
                    "error",
                    "Invalid ticket ID."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/technician/ticket-details.jsp"
            ).forward(request, response);

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/technician/ticket-details.jsp"
            ).forward(request, response);

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load ticket details.",
                    e
            );
        }
    }
}