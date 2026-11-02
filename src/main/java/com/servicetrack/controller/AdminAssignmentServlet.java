package com.servicetrack.controller;

import com.servicetrack.service.AdminAssignmentService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/assignments")
public class AdminAssignmentServlet extends HttpServlet {

    private AdminAssignmentService assignmentService;

    @Override
    public void init() {
        assignmentService = new AdminAssignmentService();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {
            request.setAttribute(
                    "tickets",
                    assignmentService.getAllTickets()
            );

            request.setAttribute(
                    "technicians",
                    assignmentService.getActiveTechnicians()
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/admin/assign-technicians.jsp"
            ).forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(
                    "Unable to load technician assignments.", e
            );
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Long ticketId = Long.parseLong(
                    request.getParameter("ticketId")
            );

            Long technicianId = Long.parseLong(
                    request.getParameter("technicianId")
            );

            boolean assigned =
                    assignmentService.assignTechnician(
                            ticketId,
                            technicianId
                    );

            if (!assigned) {
                throw new IllegalArgumentException(
                        "Ticket could not be assigned."
                );
            }

            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/assignments?success=assigned"
            );

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/assignments?error=invalid"
            );

        } catch (IllegalArgumentException e) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/assignments?error=assignment"
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to assign technician.", e
            );
        }
    }
}
