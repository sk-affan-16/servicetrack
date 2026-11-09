
package com.servicetrack.controller;

import com.servicetrack.dao.SparePartDAO;
import com.servicetrack.model.RepairPartUsage;
import com.servicetrack.model.SparePart;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/technician/parts-usage")
public class PartsUsageServlet extends HttpServlet {

    private PartsUsageService partsUsageService;
    private TicketService ticketService;
    private SparePartDAO sparePartDAO;

    @Override
    public void init() {

        partsUsageService = new PartsUsageService();
        ticketService = new TicketService();
        sparePartDAO = new SparePartDAO();
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

        Long technicianId =
                (Long) session.getAttribute("userId");

        String ticketIdParameter =
                request.getParameter("ticketId");

        if (ticketIdParameter != null &&
                !ticketIdParameter.isBlank()) {

            try {

                Long ticketId =
                        Long.parseLong(ticketIdParameter);

                if (!isTicketAssignedToTechnician(
                        ticketId,
                        technicianId)) {

                    response.sendError(
                            HttpServletResponse.SC_FORBIDDEN,
                            "You are not authorized to access this ticket."
                    );
                    return;
                }

                request.setAttribute(
                        "selectedTicketId",
                        ticketId
                );

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

        try {

            loadFormData(request, technicianId);

            forwardPartsUsagePage(request, response);

        } catch (SQLException e) {

            log("Unable to load parts usage form.", e);

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to load parts usage data."
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

        Long technicianId =
                (Long) session.getAttribute("userId");

        try {

            Long ticketId = Long.parseLong(
                    request.getParameter("ticketId")
            );

            Long sparePartId = Long.parseLong(
                    request.getParameter("sparePartId")
            );

            int quantity = Integer.parseInt(
                    request.getParameter("quantity")
            );

            request.setAttribute(
                    "selectedTicketId",
                    ticketId
            );

            if (!isTicketAssignedToTechnician(
                    ticketId,
                    technicianId)) {

                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "You are not authorized to use parts for this ticket."
                );
                return;
            }

            RepairPartUsage usage =
                    partsUsageService.usePart(
                            ticketId,
                            sparePartId,
                            quantity
                    );

            session.setAttribute(
                    "success",
                    "Spare part used successfully. Usage ID: "
                            + usage.getUsageId()
            );

            response.sendRedirect(
                    request.getContextPath()
                            + "/technician/parts-usage?ticketId="
                            + ticketId
            );

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Please provide valid ticket, spare part and quantity values."
            );

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            try {

                loadFormData(request, technicianId);

                forwardPartsUsagePage(request, response);

            } catch (SQLException sqlException) {

                log("Unable to reload parts usage form.", sqlException);

                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Unable to reload parts usage data."
                );
            }

        } catch (SQLException e) {

            log("Unable to record spare-part usage.", e);

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to record spare-part usage."
            );
        }
    }

    /*
     * Loads only the logged-in technician's tickets.
     * Loads spare parts having available stock.
     */
    private void loadFormData(
            HttpServletRequest request,
            Long technicianId)
            throws SQLException {

        List<Ticket> assignedTickets =
                ticketService.getTicketsByTechnician(
                        technicianId
                );

        List<SparePart> allParts =
                sparePartDAO.findAll();

        List<SparePart> availableParts =
                new ArrayList<>();

        for (SparePart part : allParts) {

            if (part.getQuantity() > 0) {
                availableParts.add(part);
            }
        }

        request.setAttribute(
                "assignedTickets",
                assignedTickets
        );

        request.setAttribute(
                "availableParts",
                availableParts
        );
    }

    private void forwardPartsUsagePage(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute(
                "contentPage",
                "/WEB-INF/views/technician/parts-usage.jsp"
        );

        request.getRequestDispatcher(
                "/WEB-INF/views/common/master.jsp"
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
