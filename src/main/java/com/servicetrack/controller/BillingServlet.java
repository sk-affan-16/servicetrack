
package com.servicetrack.controller;

import com.servicetrack.model.Bill;
import com.servicetrack.model.Ticket;
import com.servicetrack.service.BillingService;
import com.servicetrack.service.TicketService;

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
import java.util.Objects;

@WebServlet("/admin/billing")
public class BillingServlet extends HttpServlet {

    private BillingService billingService;
    private TicketService ticketService;

    @Override
    public void init() {

        billingService = new BillingService();
        ticketService = new TicketService();
    }

    /*
     * Display billing page.
     *
     * Loads:
     * 1. All existing bills.
     * 2. Tickets eligible for bill generation.
     */
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

            List<Ticket> eligibleTickets =
                    ticketService.getTicketsEligibleForBilling();

            request.setAttribute(
                    "bills",
                    bills
            );

            request.setAttribute(
                    "eligibleTickets",
                    eligibleTickets
            );

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

    /*
     * Process billing operations.
     *
     * Supported actions:
     * 1. generate
     * 2. markPaid
     */
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

                redirect(
                        request,
                        response,
                        "success",
                        "bill_generated"
                );

            } else if ("markPaid".equals(action)) {

                markAsPaid(request);

                redirect(
                        request,
                        response,
                        "success",
                        "payment_updated"
                );

            } else {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid billing action."
                );
            }

        } catch (NumberFormatException e) {

            redirect(
                    request,
                    response,
                    "error",
                    "invalid_input"
            );

        } catch (IllegalArgumentException e) {

            redirect(
                    request,
                    response,
                    "error",
                    "invalid_request"
            );

        } catch (SQLException e) {

            log("Unable to process billing operation.", e);

            redirect(
                    request,
                    response,
                    "error",
                    "database"
            );
        }
    }

    /*
     * Generate a bill for an eligible completed ticket.
     */
    private void generateBill(
            HttpServletRequest request)
            throws SQLException {

        String ticketValue =
                request.getParameter("ticketId");

        String labourValue =
                request.getParameter("labourCharge");

        if (ticketValue == null || ticketValue.isBlank()
                || labourValue == null || labourValue.isBlank()) {

            throw new IllegalArgumentException(
                    "Ticket ID and labour charge are required."
            );
        }

        Long ticketId =
                Long.parseLong(ticketValue.trim());

        BigDecimal labourCharge =
                new BigDecimal(labourValue.trim());

        if (labourCharge.compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "Labour charge cannot be negative."
            );
        }

        /*
         * Verify that the selected ticket:
         * 1. Is completed.
         * 2. Has its latest repair completed.
         * 3. Does not already have a bill.
         */
        List<Ticket> eligibleTickets =
                ticketService.getTicketsEligibleForBilling();

        boolean eligible =
                eligibleTickets.stream().anyMatch(ticket ->
                        Objects.equals(
                                ticket.getTicketId(),
                                ticketId
                        )
                );

        if (!eligible) {

            throw new IllegalArgumentException(
                    "Selected ticket is not eligible for billing."
            );
        }

        /*
         * BillingService performs additional validation.
         * BillingDAO performs transactional bill generation.
         */
        billingService.generateBill(
                ticketId,
                labourCharge
        );
    }

    /*
     * Mark an unpaid bill as paid.
     */
    private void markAsPaid(
            HttpServletRequest request)
            throws SQLException {

        String billValue =
                request.getParameter("billId");

        if (billValue == null || billValue.isBlank()) {

            throw new IllegalArgumentException(
                    "Bill ID is required."
            );
        }

        Long billId =
                Long.parseLong(billValue.trim());

        billingService.markBillAsPaid(billId);
    }

    /*
     * Check whether the current session belongs to an admin.
     */
    private boolean isAdmin(HttpSession session) {

        if (session == null) {
            return false;
        }

        String role =
                (String) session.getAttribute("role");

        return "ADMIN".equals(role);
    }

    /*
     * Redirect after POST to prevent duplicate form submission
     * when the browser page is refreshed.
     */
    private void redirect(
            HttpServletRequest request,
            HttpServletResponse response,
            String parameter,
            String value)
            throws IOException {

        response.sendRedirect(
                request.getContextPath()
                        + "/admin/billing?"
                        + parameter
                        + "="
                        + value
        );
    }
}
