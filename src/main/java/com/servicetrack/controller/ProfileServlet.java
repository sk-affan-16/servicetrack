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

@WebServlet("/customer/profile")
public class ProfileServlet extends HttpServlet {

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

        HttpSession session =
                request.getSession(false);

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

            User user = authService.getUserById(userId);

            request.setAttribute("user", user);

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer/profile.jsp"
            ).forward(request, response);

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer/profile.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            log("Unable to load customer profile", e);

            request.setAttribute(
                    "error",
                    "Unable to load profile. Please try again."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer/profile.jsp"
            ).forward(request, response);
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session =
                request.getSession(false);

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

        String fullName =
                request.getParameter("fullName");

        String email =
                request.getParameter("email");

        String phone =
                request.getParameter("phone");

        try {

            User updatedUser =
                    authService.updateProfile(
                            userId,
                            fullName,
                            email,
                            phone
                    );

            /*
             * Refresh the session with the updated
             * user information.
             */
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
                            + "/customer/profile?updated=true"
            );

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            request.setAttribute(
                    "fullName",
                    fullName
            );

            request.setAttribute(
                    "email",
                    email
            );

            request.setAttribute(
                    "phone",
                    phone
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer/profile.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            log("Profile update failed", e);

            request.setAttribute(
                    "error",
                    "Profile update failed. Please try again."
            );

            request.setAttribute(
                    "fullName",
                    fullName
            );

            request.setAttribute(
                    "email",
                    email
            );

            request.setAttribute(
                    "phone",
                    phone
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer/profile.jsp"
            ).forward(request, response);
        }
    }
}