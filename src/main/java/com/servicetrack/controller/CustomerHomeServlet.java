package com.servicetrack.controller;

import com.servicetrack.model.Customer;
import com.servicetrack.service.CustomerService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/customer/")
public class CustomerHomeServlet extends HttpServlet {

    private CustomerService customerService;

    @Override
    public void init() {
        customerService = new CustomerService();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        try {

            Customer customer =
                    customerService.getCustomerByUserId(userId);

            request.setAttribute(
                    "customer",
                    customer
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer-home.jsp"
            ).forward(request, response);

        } catch (SQLException e) {

            log("Unable to load customer profile.", e);

            request.setAttribute(
                    "error",
                    "Unable to load customer profile."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer-home.jsp"
            ).forward(request, response);
        }
    }
}