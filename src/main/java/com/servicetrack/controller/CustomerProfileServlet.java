package com.servicetrack.controller;

import com.servicetrack.model.Customer;
import com.servicetrack.model.User;
import com.servicetrack.service.AuthService;
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

    private AuthService authService;
    private CustomerService customerService;

    @Override
    public void init() {
        authService = new AuthService();
        customerService = new CustomerService();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            User user = authService.getUserById(userId);
            Customer customer = customerService.getCustomerByUserId(userId);

            request.setAttribute("user", user);
            request.setAttribute("customer", customer);

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer-profile.jsp"
            ).forward(request, response);

        } catch (Exception e) {
            log("Unable to load customer profile.", e);
            request.setAttribute(
                    "error",
                    "Unable to load profile. Please try again."
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
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String formType = request.getParameter("formType");

        try {

            if ("user".equals(formType)) {

                String fullName = request.getParameter("fullName");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");

                User updatedUser =
                        authService.updateProfile(
                                userId,
                                fullName,
                                email,
                                phone
                        );

                session.setAttribute(
                        "fullName",
                        updatedUser.getFullName()
                );
                session.setAttribute(
                        "username",
                        updatedUser.getUsername()
                );
                session.setAttribute(
                        "userId",
                        updatedUser.getUserId()
                );
                session.setAttribute(
                        "role",
                        updatedUser.getRole()
                );

                response.sendRedirect(
                        request.getContextPath()
                                + "/customer/profile?updated=user"
                );
                return;
            }

            if ("customer".equals(formType)) {

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

                Customer existingCustomer =
                        customerService.getCustomerByUserId(userId);

                if (existingCustomer == null) {
                    customerService.createCustomer(customer);
                } else {
                    customerService.updateCustomer(customer);
                }

                response.sendRedirect(
                        request.getContextPath()
                                + "/customer/profile?updated=customer"
                );
                return;
            }

            response.sendRedirect(
                    request.getContextPath() + "/customer/profile"
            );

        } catch (IllegalArgumentException e) {

            request.setAttribute("error", e.getMessage());

            loadProfileData(request, userId);

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer-profile.jsp"
            ).forward(request, response);

        } catch (SQLException e) {

            log("Unable to save customer profile.", e);

            request.setAttribute(
                    "error",
                    "Unable to save profile. Please try again."
            );

            loadProfileData(request, userId);

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer-profile.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            log("Profile update failed.", e);

            request.setAttribute(
                    "error",
                    "Profile update failed. Please try again."
            );

            loadProfileData(request, userId);

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer-profile.jsp"
            ).forward(request, response);
        }
    }

    private void loadProfileData(
            HttpServletRequest request,
            Long userId) {

        try {
            request.setAttribute(
                    "user",
                    authService.getUserById(userId)
            );

            request.setAttribute(
                    "customer",
                    customerService.getCustomerByUserId(userId)
            );

        } catch (Exception e) {
            log("Unable to reload profile data.", e);
        }
    }
}
