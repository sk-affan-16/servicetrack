package com.servicetrack.controller;

import com.servicetrack.model.Bill;
import com.servicetrack.service.BillingService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/customer/bills")
public class CustomerBillsServlet extends HttpServlet {

    private BillingService billingService;

    @Override
    public void init() {
        billingService = new BillingService();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Check whether the user is logged in
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        // Allow only customers
        String role = (String) session.getAttribute("role");

        if (!"CUSTOMER".equals(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            // Get authenticated user's ID from session
            Object userIdObject = session.getAttribute("userId");

            if (!(userIdObject instanceof Number)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            Long userId = ((Number) userIdObject).longValue();

            // Retrieve only this customer's bills
            List<Bill> bills =
                    billingService.getBillsByCustomer(userId);

            request.setAttribute("bills", bills);

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer/bills.jsp"
            ).forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(
                    "Unable to load customer bills.", e
            );
        }
    }
}