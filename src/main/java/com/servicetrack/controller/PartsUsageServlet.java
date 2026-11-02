package com.servicetrack.controller;

import com.servicetrack.model.RepairPartUsage;
import com.servicetrack.model.Ticket;
import com.servicetrack.service.PartsUsageService;
import com.servicetrack.service.TicketService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/technician/parts-usage")
public class PartsUsageServlet extends HttpServlet {

    private PartsUsageService partsUsageService;
    private TicketService ticketService;

    @Override
    public void init() {
        partsUsageService = new PartsUsageService();
        ticketService = new TicketService();
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (!isTechnician(session)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Technician access required."
            );
            return;
        }

        request.setCharacterEncoding("UTF-8");

        try {

            Long ticketId = Long.parseLong(
                    request.getParameter("ticketId")
            );

            Long technicianId =
                    (Long) session.getAttribute("userId");

            if (!isTicketAssignedToTechnician(
                    ticketId,
                    technicianId)) {

                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "You are not authorized to use parts for this ticket."
                );
                return;
            }

            Long sparePartId = Long.parseLong(
                    request.getParameter("sparePartId")
            );

            int quantity = Integer.parseInt(
                    request.getParameter("quantity")
            );

            RepairPartUsage usage =
                    partsUsageService.usePart(
                            ticketId,
                            sparePartId,
                            quantity
                    );

            request.getSession().setAttribute(
                    "success",
                    "Spare part used successfully. " +
                            "Usage ID: " + usage.getUsageId()
            );

            response.sendRedirect(
                    request.getContextPath()
                            + "/technician/parts-usage?ticketId="
                            + ticketId
            );

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid numeric value."
            );

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            forwardErrorPage(request, response);

        } catch (SQLException e) {

            log("Unable to record spare-part usage.", e);

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to verify ticket authorization."
            );
        }
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (!isTechnician(session)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Technician access required."
            );
            return;
        }

        String ticketIdParameter =
                request.getParameter("ticketId");

        if (ticketIdParameter != null &&
                !ticketIdParameter.isBlank()) {

            try {

                Long ticketId =
                        Long.parseLong(ticketIdParameter);

                Long technicianId =
                        (Long) session.getAttribute("userId");

                if (!isTicketAssignedToTechnician(
                        ticketId,
                        technicianId)) {

                    response.sendError(
                            HttpServletResponse.SC_FORBIDDEN,
                            "You are not authorized to access this ticket."
                    );
                    return;
                }

            } catch (NumberFormatException e) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid Ticket ID."
                );
                return;

            } catch (SQLException e) {

                log("Unable to verify ticket authorization.", e);

                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Unable to verify ticket authorization."
                );
                return;
            }
        }

        request.getRequestDispatcher(
                "/WEB-INF/views/technician/parts-usage.jsp"
        ).forward(request, response);
    }

    private void forwardErrorPage(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                "/WEB-INF/views/technician/parts-usage.jsp"
        ).forward(request, response);
    }

    private boolean isTicketAssignedToTechnician(
            Long ticketId,
            Long technicianId)
            throws SQLException {

        if (technicianId == null) {
            return false;
        }

        Ticket ticket =
                ticketService.getTicket(ticketId);

        if (ticket == null) {
            return false;
        }

        return ticket.getTechnicianId() != null
                && ticket.getTechnicianId()
                .equals(technicianId);
    }

    private boolean isTechnician(HttpSession session) {

        if (session == null) {
            return false;
        }

        String role =
                (String) session.getAttribute("role");

        return "TECHNICIAN".equals(role);
    }
}