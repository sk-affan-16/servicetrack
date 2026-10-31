package com.servicetrack.controller;

import com.servicetrack.model.AdminDashboard;
import com.servicetrack.service.AdminDashboardService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private AdminDashboardService dashboardService;

    @Override
    public void init() {
        dashboardService = new AdminDashboardService();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            AdminDashboard dashboard =
                    dashboardService.getDashboardData();

            request.setAttribute("dashboard", dashboard);

            request.getRequestDispatcher(
                    "/WEB-INF/views/admin/dashboard.jsp"
            ).forward(request, response);

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load admin dashboard.", e
            );
        }
    }
}