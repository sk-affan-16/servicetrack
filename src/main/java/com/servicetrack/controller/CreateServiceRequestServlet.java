package com.servicetrack.controller;

import com.servicetrack.model.Product;
import com.servicetrack.model.Ticket;
import com.servicetrack.service.ProductService;
import com.servicetrack.service.ServiceRequestService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/customer/service-request/create")
public class CreateServiceRequestServlet extends HttpServlet {

    private final ProductService productService =
            new ProductService();

    private final ServiceRequestService serviceRequestService =
            new ServiceRequestService();

    private static final String MASTER_PAGE =
            "/WEB-INF/views/common/master.jsp";

    private static final String FORM_PAGE =
            "/WEB-INF/views/customer/service-request-form.jsp";

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

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        String productIdParameter =
                request.getParameter("productId");

        String complaintDescription =
                request.getParameter("complaintDescription");

        try {

            if (productIdParameter == null
                    || productIdParameter.trim().isEmpty()) {

                throw new IllegalArgumentException(
                        "Please select a product."
                );
            }

            Long productId =
                    Long.parseLong(productIdParameter);

            // Verify product ownership.
            if (productService.getProduct(productId, userId) == null) {

                throw new IllegalArgumentException(
                        "Selected product was not found or does not belong to you."
                );
            }

            // Create service request and ticket.
            Ticket ticket =
                    serviceRequestService.createServiceRequestWithTicket(
                            productId,
                            complaintDescription
                    );

            // Redirect to ticket tracking page.
            response.sendRedirect(
                    request.getContextPath()
                            + "/customer/ticket?ticketId="
                            + ticket.getTicketId()
            );

        } catch (NumberFormatException e) {

            forwardWithError(
                    request,
                    response,
                    userId,
                    "Invalid product selected."
            );

        } catch (IllegalArgumentException e) {

            forwardWithError(
                    request,
                    response,
                    userId,
                    e.getMessage()
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to create service request.",
                    e
            );
        }
    }

    private void forwardWithError(
            HttpServletRequest request,
            HttpServletResponse response,
            Long userId,
            String error)
            throws ServletException, IOException {

        request.setAttribute("contentPage", FORM_PAGE);
        request.setAttribute("error", error);

        try {

            List<Product> products =
                    productService.getCustomerProducts(userId);

            request.setAttribute("products", products);

        } catch (SQLException e) {

            log("Unable to reload customer products.", e);

            request.setAttribute(
                    "error",
                    "Unable to reload products. Please try again."
            );
        }

        request.getRequestDispatcher(MASTER_PAGE)
                .forward(request, response);
    }
}