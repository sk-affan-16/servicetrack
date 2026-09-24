package com.servicetrack.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/customer/")
public class CustomerHomeServlet extends HttpServlet {

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

        String role =
                (String) session.getAttribute("role");

        if (userId == null || !"CUSTOMER".equals(role)) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        String fullName =
                (String) session.getAttribute("fullName");

        String username =
                (String) session.getAttribute("username");

        response.setContentType("text/html;charset=UTF-8");

        response.getWriter().println("""
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <title>Customer Dashboard - ServiceTrack</title>
                </head>
                <body>

                    <h1>ServiceTrack</h1>

                    <h2>Customer Dashboard</h2>

                    <p>Welcome, %s!</p>

                    <p>Username: %s</p>

                    <p>
                        You are successfully authenticated as a CUSTOMER.
                    </p>

                    <p>
                        Session authentication is working.
                    </p>

                    <hr>

                    <p>
                        <a href="%s/customer/profile">
                            Customer Profile
                        </a>
                    </p>

                    <p>
                        <a href="%s/customer/products">
                            My Products
                        </a>
                    </p>

                    <p>
                        <a href="%s/logout">
                            Logout
                        </a>
                    </p>

                </body>
                </html>
                """.formatted(
                fullName,
                username,
                request.getContextPath(),
                request.getContextPath(),
                request.getContextPath()
        ));
    }
}