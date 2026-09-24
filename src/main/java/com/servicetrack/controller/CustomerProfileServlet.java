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

@WebServlet("/customer/profile")
public class CustomerProfileServlet extends HttpServlet {

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

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        try {
            Customer customer =
                    customerService.getCustomerByUserId(userId);

            request.setAttribute("customer", customer);

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer-profile.jsp"
            ).forward(request, response);

        } catch (SQLException e) {

            log("Unable to load customer profile.", e);

            request.setAttribute(
                    "error",
                    "Unable to load customer profile."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer-profile.jsp"
            ).forward(request, response);
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String pincode = request.getParameter("pincode");

        Customer customer = new Customer();

        customer.setUserId(userId);
        customer.setAddress(address);
        customer.setCity(city);
        customer.setState(state);
        customer.setPincode(pincode);

        try {
            Customer existingCustomer =
                    customerService.getCustomerByUserId(userId);

            if (existingCustomer == null) {

                customerService.createCustomer(customer);

            } else {

                customerService.updateCustomer(customer);
            }

            response.sendRedirect(
                    request.getContextPath()
                            + "/customer/profile"
            );

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            request.setAttribute(
                    "customer",
                    customer
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer-profile.jsp"
            ).forward(request, response);

        } catch (SQLException e) {

            log("Unable to save customer profile.", e);

            request.setAttribute(
                    "error",
                    "Unable to save customer profile."
            );

            request.setAttribute(
                    "customer",
                    customer
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer-profile.jsp"
            ).forward(request, response);
        }
    }
}