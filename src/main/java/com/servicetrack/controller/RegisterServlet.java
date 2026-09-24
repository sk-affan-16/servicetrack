package com.servicetrack.controller;

import com.servicetrack.service.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private AuthService authService;

    @Override
    public void init() {
        authService = new AuthService();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                "/WEB-INF/views/register.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {

            authService.registerCustomer(
                    fullName,
                    email,
                    phone,
                    username,
                    password
            );

            response.sendRedirect(
                    request.getContextPath()
                            + "/login?registered=true"
            );

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            request.setAttribute("fullName", fullName);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.setAttribute("username", username);

            request.getRequestDispatcher(
                    "/WEB-INF/views/register.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            log("Registration failed", e);

            request.setAttribute(
                    "error",
                    "Registration failed. Please try again."
            );

            request.setAttribute("fullName", fullName);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.setAttribute("username", username);

            request.getRequestDispatcher(
                    "/WEB-INF/views/register.jsp"
            ).forward(request, response);
        }
    }
}