package com.servicetrack.controller;

import com.servicetrack.model.User;
import com.servicetrack.service.TechnicianManagementService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/technicians")
public class AdminTechniciansServlet extends HttpServlet {
    private TechnicianManagementService service;

    @Override
    public void init() {
        service = new TechnicianManagementService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("technicians", service.getTechnicians());
            request.getRequestDispatcher("/WEB-INF/views/admin/technicians.jsp")
                    .forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Unable to load technicians.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        try {
            if ("create".equals(action)) {
                User created = service.createTechnician(
                        request.getParameter("fullName"),
                        request.getParameter("email"),
                        request.getParameter("phone"),
                        request.getParameter("username"),
                        request.getParameter("password"));
                response.sendRedirect(request.getContextPath()
                        + "/admin/technicians?success=created");
                return;
            }
            if ("status".equals(action)) {
                long userId = Long.parseLong(request.getParameter("userId"));
                service.changeStatus(userId, request.getParameter("status"));
                response.sendRedirect(request.getContextPath()
                        + "/admin/technicians?success=updated");
                return;
            }
            throw new IllegalArgumentException("Invalid action.");
        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath()
                    + "/admin/technicians?error=invalid");
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                response.sendRedirect(request.getContextPath()
                        + "/admin/technicians?error=duplicate");
            } else {
                throw new ServletException("Unable to save technician.", e);
            }
        }
    }
}
