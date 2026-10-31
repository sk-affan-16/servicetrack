package com.servicetrack.controller;

import com.servicetrack.model.Repair;
import com.servicetrack.model.Ticket;
import com.servicetrack.service.RepairService;
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

@WebServlet("/technician/repair")
public class TechnicianRepairServlet extends HttpServlet {

    private RepairService repairService;
    private TicketService ticketService;

    @Override
    public void init() {
        repairService = new RepairService();
        ticketService = new TicketService();
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

        if (ticketIdParameter == null ||
                ticketIdParameter.isBlank()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Ticket ID is required."
            );
            return;
        }

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

            List<Repair> repairs =
                    repairService.getRepairsByTicket(
                            ticketId
                    );

            request.setAttribute(
                    "ticketId",
                    ticketId
            );

            request.setAttribute(
                    "repairs",
                    repairs
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/technician/repair.jsp"
            ).forward(request, response);

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid Ticket ID."
            );

        } catch (SQLException e) {

            log("Unable to load repair information.", e);

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to load repair information."
            );
        }
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

        String ticketIdParameter =
                request.getParameter("ticketId");

        try {

            if (ticketIdParameter == null ||
                    ticketIdParameter.isBlank()) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Ticket ID is required."
                );
                return;
            }

            Long ticketId =
                    Long.parseLong(ticketIdParameter);

            Long technicianId =
                    (Long) session.getAttribute("userId");

            if (!isTicketAssignedToTechnician(
                    ticketId,
                    technicianId)) {

                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "You are not authorized to modify this ticket."
                );
                return;
            }

            String action =
                    request.getParameter("action");

            if ("create".equals(action)) {

                createRepair(request);

            } else if ("update".equals(action)) {

                updateRepair(request, ticketId);

            } else {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid repair action."
                );
                return;
            }

            response.sendRedirect(
                    request.getContextPath()
                            + "/technician/repair?ticketId="
                            + ticketId
            );

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid ID."
            );

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            forwardBackToRepairPage(
                    request,
                    response
            );

        } catch (SQLException e) {

            log("Unable to save repair.", e);

            request.setAttribute(
                    "error",
                    "Unable to save repair."
            );

            forwardBackToRepairPage(
                    request,
                    response
            );
        }
    }

    private void createRepair(
            HttpServletRequest request)
            throws SQLException {

        Long ticketId =
                Long.parseLong(
                        request.getParameter("ticketId")
                );

        String diagnosis =
                request.getParameter("diagnosis");

        String repairNotes =
                request.getParameter("repairNotes");

        repairService.createRepair(
                ticketId,
                diagnosis,
                repairNotes
        );
    }

    private void updateRepair(
            HttpServletRequest request,
            Long ticketId)
            throws SQLException {

        Long repairId =
                Long.parseLong(
                        request.getParameter("repairId")
                );

        String diagnosis =
                request.getParameter("diagnosis");

        String repairNotes =
                request.getParameter("repairNotes");

        String repairStatus =
                request.getParameter("repairStatus");

        Repair existingRepair =
                repairService.getRepair(repairId);

        if (existingRepair == null) {
            throw new IllegalArgumentException(
                    "Repair not found."
            );
        }

        if (existingRepair.getTicketId() == null
                || !existingRepair.getTicketId()
                .equals(ticketId)) {

            throw new IllegalArgumentException(
                    "Repair does not belong to this ticket."
            );
        }

        repairService.updateRepair(
                repairId,
                diagnosis,
                repairNotes,
                repairStatus
        );
    }

    private void forwardBackToRepairPage(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String ticketIdParameter =
                request.getParameter("ticketId");

        if (ticketIdParameter == null ||
                ticketIdParameter.isBlank()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Ticket ID is required."
            );
            return;
        }

        try {

            Long ticketId =
                    Long.parseLong(ticketIdParameter);

            List<Repair> repairs =
                    repairService.getRepairsByTicket(
                            ticketId
                    );

            request.setAttribute(
                    "ticketId",
                    ticketId
            );

            request.setAttribute(
                    "repairs",
                    repairs
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/technician/repair.jsp"
            ).forward(request, response);

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid Ticket ID."
            );

        } catch (SQLException e) {

            log("Unable to reload repair information.", e);

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to reload repair information."
            );
        }
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

    private boolean isTechnician(
            HttpSession session) {

        if (session == null) {
            return false;
        }

        String role =
                (String) session.getAttribute("role");

        return "TECHNICIAN".equals(role);
    }
}