package com.servicetrack.controller;

import com.servicetrack.model.User;
import com.servicetrack.service.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

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
                "/WEB-INF/views/login.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {

            User user = authService.login(
                    username,
                    password
            );

            if (user == null) {

                request.setAttribute(
                        "error",
                        "Invalid username or password."
                );

                request.setAttribute(
                        "username",
                        username
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/login.jsp"
                ).forward(request, response);

                return;
            }

            /*
             * Create a new session after successful authentication.
             * This also helps prevent session fixation.
             */
            HttpSession oldSession = request.getSession(false);

            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession session = request.getSession(true);

            /*
             * Shared ServiceTrack session contract.
             */
            session.setAttribute(
                    "userId",
                    user.getUserId()
            );

            session.setAttribute(
                    "username",
                    user.getUsername()
            );

            session.setAttribute(
                    "fullName",
                    user.getFullName()
            );

            session.setAttribute(
                    "role",
                    user.getRole()
            );

            /*
             * Redirect according to the authenticated role.
             */
            switch (user.getRole()) {

                case "CUSTOMER":
                    response.sendRedirect(
                            request.getContextPath()
                                    + "/customer/"
                    );
                    break;

                case "TECHNICIAN":
                    response.sendRedirect(
                            request.getContextPath()
                                    + "/technician/"
                    );
                    break;

                case "ADMIN":
                    response.sendRedirect(
                            request.getContextPath()
                                    + "/admin/"
                    );
                    break;

                default:
                    session.invalidate();

                    request.setAttribute(
                            "error",
                            "Invalid user role."
                    );

                    request.getRequestDispatcher(
                            "/WEB-INF/views/login.jsp"
                    ).forward(request, response);
            }

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            request.setAttribute(
                    "username",
                    username
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/login.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            log("Login failed", e);

            request.setAttribute(
                    "error",
                    "Login failed. Please try again."
            );

            request.setAttribute(
                    "username",
                    username
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/login.jsp"
            ).forward(request, response);
        }
    }
}