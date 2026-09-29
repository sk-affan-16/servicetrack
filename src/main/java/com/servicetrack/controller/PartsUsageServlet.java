package com.servicetrack.controller;

import com.servicetrack.model.RepairPartUsage;
import com.servicetrack.service.PartsUsageService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/technician/parts-usage")
public class PartsUsageServlet extends HttpServlet {

    private PartsUsageService partsUsageService;

    @Override
    public void init() {
        partsUsageService = new PartsUsageService();
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (!isTechnician(session)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Technician access required."
            );
            return;
        }

        request.setCharacterEncoding("UTF-8");

        try {
            Long ticketId = Long.parseLong(
                    request.getParameter("ticketId")
            );

            Long sparePartId = Long.parseLong(
                    request.getParameter("sparePartId")
            );

            int quantity = Integer.parseInt(
                    request.getParameter("quantity")
            );

            RepairPartUsage usage =
                    partsUsageService.usePart(
                            ticketId,
                            sparePartId,
                            quantity
                    );

            request.getSession().setAttribute(
                    "success",
                    "Spare part used successfully. " +
                            "Usage ID: " + usage.getUsageId()
            );

            response.sendRedirect(
                    request.getContextPath()
                            + "/technician/parts-usage?ticketId="
                            + ticketId
            );

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid numeric value."
            );

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            forwardErrorPage(request, response);

        } catch (SQLException e) {

            log("Unable to record spare-part usage.", e);

            request.setAttribute(
                    "error",
                    "Unable to record spare-part usage."
            );

            forwardErrorPage(request, response);
        }
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (!isTechnician(session)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Technician access required."
            );
            return;
        }

        request.getRequestDispatcher(
                "/WEB-INF/views/technician/parts-usage.jsp"
        ).forward(request, response);
    }

    private void forwardErrorPage(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                "/WEB-INF/views/technician/parts-usage.jsp"
        ).forward(request, response);
    }

    private boolean isTechnician(HttpSession session) {

        if (session == null) {
            return false;
        }

        String role =
                (String) session.getAttribute("role");

        return "TECHNICIAN".equals(role);
    }
}