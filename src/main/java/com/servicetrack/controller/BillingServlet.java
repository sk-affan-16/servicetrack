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
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/billing")
public class BillingServlet extends HttpServlet {

    private BillingService billingService;

    @Override
    public void init() {
        billingService = new BillingService();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request.getSession(false))) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Admin access required."
            );
            return;
        }

        try {

            List<Bill> bills =
                    billingService.getAllBills();

            request.setAttribute("bills", bills);

            request.getRequestDispatcher(
                    "/WEB-INF/views/admin/billing.jsp"
            ).forward(request, response);

        } catch (SQLException e) {

            log("Unable to load billing information.", e);

            throw new ServletException(
                    "Unable to load billing information.",
                    e
            );
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request.getSession(false))) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Admin access required."
            );
            return;
        }

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        try {

            if ("generate".equals(action)) {

                generateBill(request);

            } else if ("markPaid".equals(action)) {

                markAsPaid(request);

            } else {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid billing action."
                );
                return;
            }

            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/billing?success=1"
            );

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/billing?error=invalid_input"
            );

        } catch (IllegalArgumentException e) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/billing?error=invalid_request"
            );

        } catch (SQLException e) {

            log("Unable to process billing operation.", e);

            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/billing?error=database"
            );
        }
    }

    private void generateBill(
            HttpServletRequest request)
            throws SQLException {

        Long ticketId =
                Long.parseLong(
                        request.getParameter("ticketId")
                );

        BigDecimal labourCharge =
                new BigDecimal(
                        request.getParameter("labourCharge")
                );

        billingService.generateBill(
                ticketId,
                labourCharge
        );
    }

    private void markAsPaid(
            HttpServletRequest request)
            throws SQLException {

        Long billId =
                Long.parseLong(
                        request.getParameter("billId")
                );

        billingService.markBillAsPaid(billId);
    }

    private boolean isAdmin(HttpSession session) {

        if (session == null) {
            return false;
        }

        String role =
                (String) session.getAttribute("role");

        return "ADMIN".equals(role);
    }
}
